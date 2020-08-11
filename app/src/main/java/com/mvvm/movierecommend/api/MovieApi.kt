package com.mvvm.movierecommend.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.QueryMap

const val BASE_URL = "https://api.themoviedb.org/3/"
const val API_KEY = "ba064b392125ad11f445e1e675280b4a"


object MovieApi {
    private val movieService:MovieService

    init {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        movieService = retrofit.create(MovieService::class.java)
    }

    fun getMovieList(@QueryMap param: Map<String, String>) = movieService.getMovieList(param)

    fun searchMovie(@QueryMap param: Map<String, String>) = movieService.searchMovie(param)
}