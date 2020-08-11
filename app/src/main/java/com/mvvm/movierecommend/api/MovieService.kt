package com.mvvm.movierecommend.api

import com.mvvm.movierecommend.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MovieService {
    @GET("discover/movie")
    fun getMovieList(@QueryMap param: Map<String, String>): Call<MovieResponse>

    @GET("search/movie")
    fun searchMovie(@QueryMap param: Map<String, String>): Call<MovieResponse>
}
