package com.mvvm.movierecommend.repository

import com.mvvm.movierecommend.api.API_KEY
import com.mvvm.movierecommend.api.MovieApi
import com.mvvm.movierecommend.model.MovieResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieRepository(val api: MovieApi = MovieApi) {

    fun getMovieList(genre: Long = 0L): Observable<MovieResponse> {
        val param = mapOf(
            "sort_by" to "popularity.desc",
            "language" to "ko",
            "page" to "1",
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
}
