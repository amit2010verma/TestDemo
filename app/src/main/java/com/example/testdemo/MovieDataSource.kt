package com.example.testdemo

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.testdemo.services.ApiServices
import com.example.testdemo.utils.State
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDataSource(
    private val apiServices: ApiServices,
    private val compositeDisposable: CompositeDisposable,
    private val apiKey: String
) :
    PageKeyedDataSource<Int, MoviesListData>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MoviesListData>) {
        updateState(State.LOADING)
        val call = apiServices.getPopularMovies(apiKey, 1)
        call.enqueue(object : Callback<MoviesListDataResponse> {
            override fun onFailure(call: Call<MoviesListDataResponse>, t: Throwable) {
                updateState(State.ERROR)
                setRetry(Action { loadInitial(params, callback) })
            }

            override fun onResponse(call: Call<MoviesListDataResponse>, response: Response<MoviesListDataResponse>) {
                updateState(State.DONE)
                val data = response.body()
                var movies = data?.movieList
                movies?.let {
                    callback.onResult(it, null, 2)
                }

            }
        })

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MoviesListData>) {
        updateState(State.LOADING)
        val call = apiServices.getPopularMovies(apiKey, params.key)
        call.enqueue(object : Callback<MoviesListDataResponse> {
            override fun onFailure(call: Call<MoviesListDataResponse>, t: Throwable) {
                updateState(State.ERROR)
                setRetry(Action { loadAfter(params, callback) })
            }

            override fun onResponse(call: Call<MoviesListDataResponse>, response: Response<MoviesListDataResponse>) {
                updateState(State.DONE)
                val data = response.body()
                var movies = data?.movieList
                movies?.let {
                    callback.onResult(it, params.key + 1)
                }

            }
        })

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MoviesListData>) {

    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }


}