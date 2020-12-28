package com.first.gameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.snackbar.Snackbar;
import com.travelblog.http.Blog;
import com.travelblog.http.BlogArticlesCallback;
import com.travelblog.http.BlogHttpClient;

import java.util.List;

public class BlogDetailsActivity extends AppCompatActivity {
    private static final String EXTRAS_BLOG = "EXTRAS_BLOG";
    private ImageView imageMain;
    private ImageView imageAvatar;
    private TextView textDate;
    private TextView textTitle;
    private TextView textAuthor;
    private RatingBar ratingBar;
    private TextView textRating;
    private TextView textDescription;
    private TextView textViews;
    private  ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_blog_details);


        imageMain = findViewById(R.id.imageMain);

        imageAvatar=findViewById(R.id.imageAvatar);

        ImageView imageBack=findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v-> finish());

         textDate = findViewById(R.id.textDate);
         textTitle = findViewById(R.id.textTitle);
         textAuthor = findViewById(R.id.textAuthor);
         ratingBar=findViewById(R.id.ratingBar);
         textRating = findViewById(R.id.textRating);
         textViews = findViewById(R.id.textViews);
         textDescription = findViewById(R.id.textDescription);
         pb=findViewById(R.id.progressBar);
        showData((Blog) getIntent().getSerializableExtra(EXTRAS_BLOG));


    }

    public static void startBlogDetailsActivity(Activity activity, Blog blog) {
        Intent intent = new Intent(activity, BlogDetailsActivity.class);
        intent.putExtra(EXTRAS_BLOG, blog);
        activity.startActivity(intent);
    }

//    private void loadData() {
//        BlogHttpClient client= BlogHttpClient.INSTANCE;
//        client.loadBlogArticles(new BlogArticlesCallback() {
//            @Override
//            public void onSuccess(List<Blog> blogList) {
//                runOnUiThread(() -> showData(blogList.get(0)));
//            }
//
//            @Override
//            public void onError() {
//                runOnUiThread(()->showErrorSnackbar());
//            }
//        });
//    }
//
//    private void showErrorSnackbar() {
//        View rootView=findViewById(android.R.id.content);
//        Snackbar snackbar =Snackbar.make(rootView,"error connection",Snackbar.LENGTH_INDEFINITE);
//        snackbar.setActionTextColor(getResources().getColor(R.color.orange500));
//        snackbar.setAction("Retry",v->{
//           loadData();
//           snackbar.dismiss();
//        });
//        snackbar.show();
//    }

    private void showData(Blog blog){
        pb.setVisibility(View.GONE);
        textTitle.setText(blog.getTitle());
        textDate.setText(blog.getDate());
        textAuthor.setText(blog.getAuthor().getName());
        textRating.setText(String.valueOf(blog.getRating()));
        textViews.setText(String.format("(%d views)", blog.getViews()));
        textDescription.setText(Html.fromHtml(blog.getDescription()));
        ratingBar.setRating(blog.getRating());
        ratingBar.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(blog.getImageURL())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageMain);

        Glide.with(this)
                .load(blog.getAuthor().getAvatarURL())
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageAvatar);
    }
}