package com.travelblog.http;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
public class Blog implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String date;
    private int views;
    private float rating;
    private String image;
    private String description;
    @Embedded
    private Author author;

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("MMMM dd, yyyy");

    public Blog(String id, String title, String date, int views, float rating, String image, String description, Author author) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.views = views;
        this.rating = rating;
        this.image = image;
        this.description = description;
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getViews() {
        return views;
    }

    public float getRating() {
        return rating;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getImageURL() {
        return BlogHttpClient.getBaseUrl() + BlogHttpClient.getPATH() + getImage();
    }

    public Long getDateMillis() {
        try {
            Date date = dateFormat.parse(getDate()); // 2
            return date != null ? date.getTime() : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", views=" + views +
                ", rating=" + rating +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return views == blog.views &&
                Float.compare(blog.rating, rating) == 0 &&
                Objects.equals(id, blog.id) &&
                Objects.equals(author, blog.author) &&
                Objects.equals(title, blog.title) &&
                Objects.equals(date, blog.date) &&
                Objects.equals(image, blog.image) &&
                Objects.equals(description, blog.description);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, author, title, date, image, description, views, rating);
    }


}
