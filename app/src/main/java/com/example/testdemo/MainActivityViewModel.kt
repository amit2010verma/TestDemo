package com.example.testdemo


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.testdemo.model.MovieDataSourceFactory
import com.example.testdemo.services.ApiServices
import com.example.testdemo.utils.State
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel : ViewModel() {


    private var dataSource: LiveData<MovieDataSource>


    private val apiServices = ApiServices.getService()
    var moviesList: LiveData<PagedList<MoviesListData>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 10
    private val factory: MovieDataSourceFactory

    init {

        factory = MovieDataSourceFactory(compositeDisposable, apiServices, "99dd61b75979175f06b4103ac54707e3")
        dataSource = factory.movieDataSourceLiveData
        var config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(pageSize)
            .setPrefetchDistance(4)
            .build()
        moviesList = LivePagedListBuilder(factory, config)
            .build()



    }

    fun getState(): LiveData<State> = Transformations.switchMap<MovieDataSource,
            State>(factory.movieDataSourceLiveData, MovieDataSource::state)

    fun retry() {
        factory.movieDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return moviesList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
