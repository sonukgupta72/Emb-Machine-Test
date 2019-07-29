package com.sonukgupta72.embibe.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class MovieDataModel implements Serializable {

    @SerializedName("rowId")
    @PrimaryKey(autoGenerate = true)
    Integer rowId;

    @SerializedName("imdbID")
    @ColumnInfo
    String id;

    @SerializedName("imdbRating")
    @ColumnInfo
    String rating;

    @SerializedName("Released")
    @ColumnInfo
    String releasedDate;

    @SerializedName("imdbVotes")
    @ColumnInfo
    String vote;

    @SerializedName("Title")
    @ColumnInfo
    String title;

    @SerializedName("imageURL")
    @ColumnInfo
    String imgUrl;

    @SerializedName("detailsUrl")
    @ColumnInfo
    String detailsUrl;

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
}
