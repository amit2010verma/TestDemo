package com.example.testdemo.model

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.testdemo.MovieDataSource
import com.example.testdemo.MoviesListData
import com.example.testdemo.services.ApiServices
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: ApiServices, private val apiKey: String
) : DataSource.Factory<Int, MoviesListData>() {

    val movieDataSourceLiveData = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, MoviesListData> {
        val newsDataSource = MovieDataSource(networkService, compositeDisposable, apiKey)
        movieDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}