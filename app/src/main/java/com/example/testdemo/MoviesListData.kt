package com.example.testdemo

import com.google.gson.annotations.SerializedName

data class MoviesListData(
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("title") val title: String
) {
}