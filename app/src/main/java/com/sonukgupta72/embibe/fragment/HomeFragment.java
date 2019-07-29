package com.sonukgupta72.embibe.fragment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sonukgupta72.embibe.activity.HomeActivity;
import com.sonukgupta72.embibe.adapter.MovieListAdapter;
import com.sonukgupta72.embibe.listener.ItemClickListener;
import com.sonukgupta72.embibe.model.MovieDataModel;
import com.sonukgupta72.embibe.R;
import com.sonukgupta72.embibe.receiver.AlarmReceiver;
import com.sonukgupta72.embibe.sqliteHelper.SQLiteHelperClass;

import java.util.ArrayList;
import java.util.List;

import static com.sonukgupta72.embibe.activity.HomeActivity.FRAGMENT_DETAILS;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ItemClickListener {

    public static final String ROW_ID = "rowID";
    View view;
    RecyclerView rvMovieList;
    TextView tvEmptyList;
    private SearchView search;
    private List<MovieDataModel> movieDataModels = new ArrayList<>();
    private MovieListAdapter movieListAdapter;
    Context context;
    AlarmReceiver alR;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        rvMovieList = view.findViewById(R.id.rvMovieList);
        tvEmptyList = view.findViewById(R.id.tvEmptyList);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        initView();
        if (movieDataModels != null
                && movieDataModels.size() > 0) {
            rvMovieList.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.GONE);
        } else {
            rvMovieList.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(MovieDataModel movieDataModel) {
        if (getActivity() == null
                || movieDataModel == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ROW_ID, movieDataModel.getRowId());
        ((HomeActivity) getActivity()).changeFragment(FRAGMENT_DETAILS, bundle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        if (menu.findItem(R.id.action_search) == null) {
            return;
        }
        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (search == null) {
            return;
        }
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (movieListAdapter != null)
                    movieListAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void initView(){
        getDataList();
        movieListAdapter = new MovieListAdapter(getActivity(), movieDataModels, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvMovieList.setLayoutManager(mLayoutManager);
        rvMovieList.setItemAnimator(new DefaultItemAnimator());
        rvMovieList.setAdapter(movieListAdapter);

    }

    private void getDataList() {
        movieDataModels.clear();
        SQLiteHelperClass sqLiteHelperClass = new SQLiteHelperClass(getActivity());
        movieDataModels.addAll(sqLiteHelperClass.getAllMovieList());
    }

}
