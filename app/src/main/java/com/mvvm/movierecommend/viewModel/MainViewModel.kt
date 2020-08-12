package com.mvvm.movierecommend.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Movie
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvm.movierecommend.model.MovieItem
import com.mvvm.movierecommend.repository.MovieRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var liveMovieList: MutableLiveData<ArrayList<MovieItem>> = MutableLiveData()
    var movieList: ArrayList<MovieItem> = arrayListOf()
    var movieRepository = MovieRepository()
    var detailItem:MovieItem? = null


    @SuppressLint("CheckResult")
    fun getMovieList(genre: Long = 0L) {
        movieRepository.getMovieList()
            .subscribe({ movieResponse ->
                Log.d("fhrm", "MainViewModel -getMovieList(),    viewmodel")
                setList(movieResponse.results)
            }, { e ->
                Log.d("fhrm", "MainViewModel -getMovieList(),    : ${e.message}")
            })
    }

    private fun setList(newList: ArrayList<MovieItem>) {
        movieList = newList
        liveMovieList.value = movieList
    }


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(application) as T
        }
    }
}