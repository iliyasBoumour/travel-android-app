package com.travelblog.repository;

import android.content.Context;

import com.travelblog.database.AppDatabase;
import com.travelblog.database.BlogDao;
import com.travelblog.database.DatabaseProvider;
import com.travelblog.http.Blog;
import com.travelblog.http.BlogHttpClient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BlogRepository {
    private BlogHttpClient httpClient;
    private AppDatabase database;
    private Executor executor;

    public BlogRepository(Context context) {
        httpClient = BlogHttpClient.INSTANCE;
        database = DatabaseProvider.getInstance(context.getApplicationContext());
        executor = Executors.newSingleThreadExecutor();
    }

    public interface DataFromDatabaseCallback {
        void onSuccess(List<Blog> blogList);
    }
    public void loadDataFromDatabase(DataFromDatabaseCallback callback) {
        executor.execute(() -> callback.onSuccess(database.blogDao().getAll()));
    }

    public interface DataFromNetworkCallback {
        void onSuccess(List<Blog> blogList);
        void onError();
    }
    public void loadDataFromNetwork(DataFromNetworkCallback callback) {
        executor.execute(() -> {
            List<Blog> blogList = httpClient.loadBlogArticles();
            if (blogList == null) {
                callback.onError();
            } else {
                BlogDao blogDAO = database.blogDao();
                blogDAO.deleteAll();
                blogDAO.insertAll(blogList);
                callback.onSuccess(blogList);
            }
        });
    }
}
