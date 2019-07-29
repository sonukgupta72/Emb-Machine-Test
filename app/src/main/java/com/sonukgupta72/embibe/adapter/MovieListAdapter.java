package com.sonukgupta72.embibe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sonukgupta72.embibe.R;
import com.sonukgupta72.embibe.listener.ItemClickListener;
import com.sonukgupta72.embibe.model.MovieDataModel;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> implements Filterable {
    private static final String RATING_OUT_OF = "/10";
    Context context;
    List<MovieDataModel> movieDataModels;
    List<MovieDataModel> searchResultMovieDataModels;
    ItemClickListener itemClickListener;

    public MovieListAdapter(Context context, List<MovieDataModel> movieDataModels, ItemClickListener itemClickListener) {
        this.context = context;
        this.movieDataModels = movieDataModels;
        this.searchResultMovieDataModels = movieDataModels;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup ,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final MovieDataModel movieDataModel = searchResultMovieDataModels.get(myViewHolder.getAdapterPosition());

        myViewHolder.tvTitle.setText(movieDataModel.getRowId() + ". " + movieDataModel.getTitle());
        myViewHolder.tvRating.setText(movieDataModel.getRating() + RATING_OUT_OF);
        myViewHolder.tvReleaseDate.setText(movieDataModel.getReleasedDate());
        Glide.with(context)
                .load(movieDataModel.getImgUrl())
                .into(myViewHolder.ivPoster);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(movieDataModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResultMovieDataModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPoster;
        TextView tvTitle, tvRating, tvReleaseDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate);
        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    Filter mFilter = new Filter() {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence charSequence) {
            String charString = charSequence.toString().toLowerCase();

            if (charString.isEmpty()) {
                searchResultMovieDataModels = movieDataModels;
            } else {
                List<MovieDataModel> filteredList = new ArrayList<>();
                for (MovieDataModel row : movieDataModels) {

                    if (!TextUtils.isEmpty(row.getTitle())
                            && row.getTitle().toLowerCase().contains(charString)) {
                        filteredList.add(row);
                    }
                }
                searchResultMovieDataModels = filteredList;
            }

            Filter.FilterResults filterResults = new Filter.FilterResults();
            filterResults.values = searchResultMovieDataModels;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            notifyDataSetChanged();
        }
    };
}
