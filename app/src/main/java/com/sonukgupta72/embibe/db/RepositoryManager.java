package com.sonukgupta72.embibe.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.sonukgupta72.embibe.model.MovieDataModel;

import java.util.List;

public class RepositoryManager {

    private static MovieDataBase movieDataBase;

    public static RepositoryManager getRepositoryManager(Context context) {
        movieDataBase = getMovieDatabase(context);
        return new RepositoryManager();
    }


    /**
     * either use this api or getRepositoryManager not both at the same time
     *
     * @return
     */
    public static MovieDataBase getMovieDatabase(Context context) {
        if (movieDataBase != null) {
            return movieDataBase;
        } else {
            return movieDataBase = Room.databaseBuilder(context, MovieDataBase.class, "movie_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
    }

    public void addModel(final MovieDataModel movieDataModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                movieDataBase.dataDao().insert(movieDataModel);
            }
        }).start();
    }

    public void getAll(OnMovieListListener movieListListener) {
        new FetcherAsync(movieListListener).execute();
    }

    public void getItemById(int rowId, OnMovieListener movieListener) {
        new SingleItemFetcherAsync(movieListener).execute(rowId);
    }

    public interface OnMovieListListener {
        void onMovieList(List<MovieDataModel> entityList);
    }

    /**
     * async class for performing in background
     */
    private static class FetcherAsync extends AsyncTask<Void, Void, List<MovieDataModel>> {
        private OnMovieListListener movieListListener;

        FetcherAsync(OnMovieListListener movieListListener) {
            this.movieListListener = movieListListener;
        }

        @Override
        protected List<MovieDataModel> doInBackground(Void... voids) {
            return movieDataBase.dataDao().getAll();
        }

        @Override
        protected void onPostExecute(List<MovieDataModel> entityList) {
            super.onPostExecute(entityList);
            movieListListener.onMovieList(entityList);
        }
    }

    /**
     * listener to fetch single item
     */
    public interface OnMovieListener {
        void onMovieList(MovieDataModel item);
    }

    /**
     * async class for performing in background
     */
    private static class SingleItemFetcherAsync extends AsyncTask<Integer, Void, MovieDataModel> {
        private OnMovieListener movieListener;

        SingleItemFetcherAsync(OnMovieListener movieListener) {
            this.movieListener = movieListener;
        }

        @Override
        protected MovieDataModel doInBackground(Integer... integers) {
            return movieDataBase.dataDao().getDataById(integers[0]);
        }


        @Override
        protected void onPostExecute(MovieDataModel item) {
            super.onPostExecute(item);
            movieListener.onMovieList(item);
        }
    }
}
