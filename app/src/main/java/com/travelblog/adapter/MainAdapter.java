package com.travelblog.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.first.gameapp.BlogDetailsActivity;
import com.first.gameapp.R;
import com.travelblog.http.Blog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainAdapter extends
        ListAdapter<Blog, MainAdapter.MainViewHolder> {
    private static final String EXTRAS_BLOG = "EXTRAS_BLOG";
    private List<Blog> originalList = new ArrayList<>();
    private Context context;
    public MainAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_main, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindTo(getItem(position));
        holder.linearLayout.setOnClickListener(view -> BlogDetailsActivity.startBlogDetailsActivity((Activity) context,getItem(position)));
    }


    private static final DiffUtil.ItemCallback<Blog> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Blog>() {
                @Override
                public boolean areItemsTheSame(@NonNull Blog oldData,
                                               @NonNull Blog newData) {
                    return oldData.getId().equals(newData.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Blog oldData,
                                                  @NonNull Blog newData) {
                    return oldData.equals(newData);
                }
            };

    public void sortByTitle() {
        List<Blog> currentList = new ArrayList<>(getCurrentList()); // 1
        Collections.sort(currentList,
                (o1, o2) -> o1.getTitle().compareTo(o2.getTitle())); // 2
        submitList(currentList);
    }

    public void sortByDate() {
        List<Blog> currentList = new ArrayList<>(getCurrentList());
        Collections.sort(currentList,
                (o1, o2) -> o2.getDateMillis().compareTo(o1.getDateMillis()));
        submitList(currentList);
    }
    public void setData(@Nullable List<Blog> list) {
        originalList = list;
        super.submitList(list);
    }
    public void filter(String query) {
        List<Blog> filteredList = new ArrayList<>();
        for (Blog blog : originalList) { // 1
            if (blog.getTitle().toLowerCase().contains(query.toLowerCase())) { // 2
                filteredList.add(blog);
            }
        }
        submitList(filteredList); // 3
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textDate;
        private ImageView imageAvatar;
        private LinearLayout linearLayout;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDate = itemView.findViewById(R.id.textDate);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
            linearLayout=itemView.findViewById(R.id.item);
        }

        void bindTo(Blog blog) {
            textTitle.setText(blog.getTitle());
            textDate.setText(blog.getDate());

            Glide.with(itemView)
                    .load(blog.getAuthor().getAvatarURL())
                    .transform(new CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageAvatar);
        }
    }
}
