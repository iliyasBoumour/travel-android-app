package com.travelblog.http;

import android.util.Log;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class BlogHttpClient {
    public static final BlogHttpClient INSTANCE = new BlogHttpClient();
    private static final String BASE_URL =
            "https://bitbucket.org/dmytrodanylyk/travel-blog-resources";
    private  static  final String PATH="/raw/3eede691af3e8ff795bf6d31effb873d484877be";
    private static final String BLOG_ARTICLES_URL =
            BASE_URL + PATH +"/blog_articles.json";
    private OkHttpClient client;
    private Gson gson;

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getPATH() {
        return PATH;
    }

    private BlogHttpClient(){
        client = new OkHttpClient();
        gson = new Gson();
    }
    public List<Blog> loadBlogArticles(){
        Request req=new Request.Builder()
            .get().url(BLOG_ARTICLES_URL).build();
            try {
                Response res=client.newCall(req).execute();
                ResponseBody rb=res.body();
                if (rb!=null){
                    String json=rb.string();
                    BlogData blogData=gson.fromJson(json,BlogData.class);
                    if (blogData!=null){
                        return blogData.getData();
                    }
                }
            } catch (IOException e) {
                Log.e("BlogHttpClient", "Error loading blog articles", e);
            }
            return null;
    }
}
