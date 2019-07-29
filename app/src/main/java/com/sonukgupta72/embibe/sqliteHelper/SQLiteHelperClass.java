package com.sonukgupta72.embibe.sqliteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sonukgupta72.embibe.model.MovieDataModel;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelperClass extends SQLiteOpenHelper {
    private static final String GITHUB_BASEURL = "https://raw.githubusercontent.com/hjorturlarsen/IMDB-top-100/master/data/images/";
    private static final String FILE_FORMAT = ".jpg";
    private static final String IMDB_BASE_URL = "https://m.imdb.com/title/";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "imdbMovieList";
    private static final String TABLE_MOVIE_LIST = "movie";
    private static final String KEY_ID = "id";
    private static final String KEY_IMDB_ID = "imdbId";
    private static final String KEY_TITLE = "title";
    private static final String KEY_RATING = "rating";
    private static final String KEY_RELEASE_DATE = "releaseDate";
    private static final String KEY_IMAGE_URL = "imgUrl";
    private static final String KEY_WEB_DETAILS_URL = "detailedUrl";

    public SQLiteHelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MOVIE_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_IMDB_ID + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_RATING + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_WEB_DETAILS_URL + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_LIST);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addMovie(MovieDataModel movieDataModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMDB_ID, movieDataModel.getId()); // Contact Name
        values.put(KEY_TITLE, movieDataModel.getTitle());
        values.put(KEY_RATING, movieDataModel.getRating());
        values.put(KEY_RELEASE_DATE, movieDataModel.getReleasedDate());
        values.put(KEY_IMAGE_URL, (GITHUB_BASEURL + movieDataModel.getId() + FILE_FORMAT));
        values.put(KEY_WEB_DETAILS_URL, IMDB_BASE_URL + movieDataModel.getId() + "/");

        // Inserting Row
        db.insert(TABLE_MOVIE_LIST, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<MovieDataModel> getAllMovieList() {
        List<MovieDataModel> movieDataModels = new ArrayList<MovieDataModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MovieDataModel movieDataModel = new MovieDataModel();
                movieDataModel.setRowId(cursor.getInt(0));
                movieDataModel.setId(cursor.getString(1));
                movieDataModel.setTitle(cursor.getString(2));
                movieDataModel.setRating(cursor.getString(3));
                movieDataModel.setReleasedDate(cursor.getString(4));
                movieDataModel.setImgUrl(cursor.getString(5));
                movieDataModel.setDetailsUrl(cursor.getString(6));
                movieDataModels.add(movieDataModel);
            } while (cursor.moveToNext());
        }
        return movieDataModels;
    }

    public MovieDataModel getAllMovieFromRowID(int id) {
        // Select Query
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE_LIST + " WHERE " + KEY_ID + "=" + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        MovieDataModel movieDataModel;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                movieDataModel = new MovieDataModel();
                movieDataModel.setRowId(cursor.getInt(0));
                movieDataModel.setId(cursor.getString(1));
                movieDataModel.setTitle(cursor.getString(2));
                movieDataModel.setRating(cursor.getString(3));
                movieDataModel.setReleasedDate(cursor.getString(4));
                movieDataModel.setImgUrl(cursor.getString(5));
                movieDataModel.setDetailsUrl(cursor.getString(6));
            } while (cursor.moveToNext());
            return movieDataModel;
        } else {
            return null;
        }
    }
}
