package com.sonukgupta72.embibe.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.sonukgupta72.embibe.R;
import com.sonukgupta72.embibe.db.RepositoryManager;
import com.sonukgupta72.embibe.model.MovieDataModel;

import static com.sonukgupta72.embibe.fragment.HomeFragment.ROW_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnClickListener{

    View view;
    WebView webView;
    Button btnPrev, btnNext;
    Integer rowId;
    MovieDataModel movieDataModel;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        webView = view.findViewById(R.id.webView);
        btnPrev = view.findViewById(R.id.btnPrev);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (getArguments() != null) {
            rowId = getArguments().getInt(ROW_ID, -1);
            if (rowId>=0) {
                getMovieModel(rowId);
            } else {
                Toast.makeText(getActivity(), "OOps! Something went wrong.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getMovieModel(int rowId) {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager(getActivity());
        repositoryManager.getItemById(rowId, new RepositoryManager.OnMovieListener() {
            @Override
            public void onMovieList(MovieDataModel item) {
                movieDataModel = item;
                loadWebView();
            }
        });
    }

    private void loadWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (movieDataModel != null
                && !TextUtils.isEmpty(movieDataModel.getDetailsUrl())) {
            webView.loadUrl(movieDataModel.getDetailsUrl());
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (Uri.parse(url).getHost().equals(movieDataModel.getDetailsUrl())) {
                        // This is my website, so do not override; let my WebView load the page
                        return false;
                    }
                    // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            });
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnNext:
                if (rowId<100){
                    getMovieModel(++rowId);
                    loadWebView();
                }
                break;
            case R.id.btnPrev:
                if (rowId>1){
                    getMovieModel(--rowId);
                    loadWebView();
                }
                break;
        }
    }
}
