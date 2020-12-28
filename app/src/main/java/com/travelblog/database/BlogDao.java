package com.travelblog.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.travelblog.http.Blog;

import java.util.List;

@Dao
public interface BlogDao {
    @Query("select * from blog")
    List<Blog> getAll();

    @Insert
    void insertAll(List<Blog> list);

    @Query("delete from blog")
    void deleteAll();
}
