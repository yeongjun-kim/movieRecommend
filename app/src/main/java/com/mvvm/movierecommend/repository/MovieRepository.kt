package com.mvvm.movierecommend.repository

import android.util.Log
import com.mvvm.movierecommend.api.API_KEY
import com.mvvm.movierecommend.api.MovieApi
import com.mvvm.movierecommend.model.MovieResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieRepository(val api: MovieApi = MovieApi) {

    fun getMovieList(page: Int): Observable<MovieResponse> {
        val param = mapOf(
            "sort_by" to "popularity.desc",
            "language" to "ko",
            "page" to page.toString(),
            "api_key" to API_KEY
        )

        var observer =
            Observable.create<MovieResponse> { subscriber ->
                val call = api.getMovieList(param)
                val respnose = call.execute()
                val body = respnose.body()

                if (respnose.isSuccessful && body != null) subscriber.onNext(body)
            }

        return observer
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getGenreMovieList(page: Int, genre: String?, sortBy: String): Observable<MovieResponse> {
        val param = mutableMapOf(
            "sort_by" to sortBy,
            "language" to "ko",
            "page" to page.toString(),
            "api_key" to API_KEY
        )
        if (!genre.isNullOrBlank()) param["with_genres"] = genre


        var observer =
            Observable.create<MovieResponse> { subscriber ->
                val call = api.getMovieList(param)
                val respnose = call.execute()
                val body = respnose.body()

                if (respnose.isSuccessful && body != null) subscriber.onNext(body)
            }

        return observer
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun searchMovie(moveName: String = "", page: Int = 1): Observable<MovieResponse> {
        val param = mapOf(
            "api_key" to API_KEY,
            "language" to "ko",
            "query" to moveName,
            "page" to page.toString()
        )

        var observer =
            Observable.create<MovieResponse> { subscriber ->
                val call = api.searchMovie(param)
                val response = call.execute()
                val body = response.body()

                if (response.isSuccessful && body != null) subscriber.onNext(body)
            }

        return observer
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

}
