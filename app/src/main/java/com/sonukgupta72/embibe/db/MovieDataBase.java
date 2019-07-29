package com.sonukgupta72.embibe.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sonukgupta72.embibe.model.MovieDataModel;

@Database(entities = {MovieDataModel.class}, version = 1)
public abstract class MovieDataBase extends RoomDatabase {
    public abstract DataDao dataDao();
}
