package com.first.gameapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.travelblog.adapter.MainAdapter;
import com.travelblog.http.Blog;
import com.travelblog.http.BlogArticlesCallback;
import com.travelblog.http.BlogHttpClient;
import com.travelblog.repository.BlogRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;
    private MaterialToolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private BlogRepository repository;
    private static final int SORT_TITLE = 0;
    private static final int SORT_DATE = 1;
    private int currentSort = SORT_DATE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MainAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        repository = new BlogRepository(getApplicationContext());
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this::loadDataFromNetwork);



        toolbar = findViewById(R.id.tool);
        MenuItem searchItem = toolbar.getMenu().findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText); // 4
                return true;
            }
        });

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort) {
                onSortClicked();
            }
            return false;
        });

        loadDataFromDatabase();
        loadDataFromNetwork();
    }

    private void loadDataFromNetwork() {
        refreshLayout.setRefreshing(true);
        repository.loadDataFromNetwork(new BlogRepository.DataFromNetworkCallback() {
            @Override
            public void onSuccess(List<Blog> blogList) {
                runOnUiThread(() -> {
                    adapter.setData(blogList);
                    sortData();
                    refreshLayout.setRefreshing(false);
                });
            }

            @Override
            public void onError() {
                runOnUiThread(()->{
                    refreshLayout.setRefreshing(false);
                    showErrorSnackbar();
                });
            }
        });
    }
    private void loadDataFromDatabase() {
        repository.loadDataFromDatabase(blogList -> runOnUiThread(() -> {
            adapter.setData(blogList);
            sortData();
        }));
    }
    private void onSortClicked() {
        String[] items = {"Title", "Date"};
        new MaterialAlertDialogBuilder(this)
                .setTitle("Sort By :")
                .setSingleChoiceItems(items, currentSort, (dialog, which) -> {
                    dialog.dismiss();
                    currentSort = which;
                    sortData();
                }).show();
    }

    private void sortData() {
        if (currentSort == SORT_TITLE) {
            adapter.sortByTitle();
        } else if (currentSort == SORT_DATE) {
            adapter.sortByDate();
        }
    }

//    private void loadData() {
//        BlogHttpClient.INSTANCE.loadBlogArticles(new BlogArticlesCallback() {
//            @Override
//            public void onSuccess(List<Blog> blogList) {
//                runOnUiThread(()->{
//                    adapter.setData(blogList);
//                    sortData();
//                });
//            }
//
//            @Override
//            public void onError() {
//                runOnUiThread(()->{
//                    refreshLayout.setRefreshing(false);
//                    showErrorSnackbar();
//                });
//            }
//        });
//    }

    private void showErrorSnackbar() {
        View rootView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView,
                "Error during loading blog articles", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.orange500));
        snackbar.setAction("Retry", v -> {
            loadDataFromDatabase();
            loadDataFromNetwork();
            snackbar.dismiss();
        });
        snackbar.show();

    }
}