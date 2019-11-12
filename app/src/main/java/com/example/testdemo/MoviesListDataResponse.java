package com.example.testdemo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesListDataResponse {
    @SerializedName("page")
    private String page;

    @SerializedName("results")
    private List<MoviesListData> movieList;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<MoviesListData> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<MoviesListData> movieList) {
        this.movieList = movieList;
    }
}
