package com.travelblog.http;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.Objects;

public class Author implements Serializable {
    private String name;
    private String avatar;

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatarURL(){
        return BlogHttpClient.getBaseUrl()+BlogHttpClient.getPATH()+getAvatar();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(name, author.name) &&
                Objects.equals(avatar, author.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, avatar);
    }



}
