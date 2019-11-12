package com.example.testdemo.services

import android.util.Log
import com.example.testdemo.MoviesListDataResponse
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Query


interface ApiServices {


    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") user: String, @Query("page") page: Int): Call<MoviesListDataResponse>

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        fun getService(): ApiServices {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiServices::class.java)
        }

    }
}