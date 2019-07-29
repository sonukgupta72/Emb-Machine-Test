package com.sonukgupta72.embibe.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sonukgupta72.embibe.model.MovieDataModel;

import java.util.List;

@Dao
public interface DataDao {
    @Query("SELECT * FROM movieDataModel")
    List<MovieDataModel> getAll();

    @Query("SELECT * FROM movieDataModel WHERE rowId = :rowId;")
    MovieDataModel getDataById(int rowId);

    @Insert
    void insert(MovieDataModel dataModel);

    @Delete
    void delete(MovieDataModel dataModel);

    @Update
    void update(MovieDataModel dataModel);
}
