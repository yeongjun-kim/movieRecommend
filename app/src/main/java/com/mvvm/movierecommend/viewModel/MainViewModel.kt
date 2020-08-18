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
import com.mvvm.movierecommend.view.MainActivity.Companion.MODE_MAIN
import com.mvvm.movierecommend.view.MainActivity.Companion.MODE_SEARCH

class MainViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * mainLiveMovieList: MainFragment 에서 observe 할 list
     * searchLiveMovieList: SearchFragment 에서 observe 할 list
     * movieRepository: Repository
     * detailItem: Fragment 에서 item click 시 detailFragment 에 전달할 item
     * mainPage: MainFragment 의 페이지
     * searchPage: SearchFragmnet 의 페이지
     */

    var mainLiveMovieList: MutableLiveData<ArrayList<MovieItem>> = MutableLiveData()
    var searchLiveMovieList: MutableLiveData<ArrayList<MovieItem>> = MutableLiveData()
    var mainMovieList: ArrayList<MovieItem> = arrayListOf()
    var searchMovieList: ArrayList<MovieItem> = arrayListOf()
    var mainPage = 1
    var searchPage = 1

    var movieRepository = MovieRepository()
    var detailItem: MovieItem? = null

    @SuppressLint("CheckResult")
    fun getMovieList(page: Int = 1,genre: Long = 0L) {
        movieRepository.getMovieList(page)
            .subscribe({ movieResponse ->
                setList(MODE_MAIN, movieResponse.results)
            }, { e ->
                Log.d("fhrm", "MainViewModel -getMovieList(),    : ${e.message}")
            })
    }

    @SuppressLint("CheckResult")
    fun searchMovie(movieName: String, page:Int = 1) {
        movieRepository.searchMovie(movieName,page)
            .subscribe({ movieResponse ->
                setList(MODE_SEARCH, movieResponse.results)
            }, { e ->
                Log.d("fhrm", "MainViewModel -getMovieList(),    : ${e.message}")
            })
    }

    private fun setList(MODE: Int, newList: ArrayList<MovieItem>) {
        if (MODE == MODE_MAIN) {
            mainMovieList.addAll(newList)
            mainLiveMovieList.value = mainMovieList
        } else if (MODE == MODE_SEARCH) {
            searchMovieList.addAll(newList)
            searchLiveMovieList.value = searchMovieList
        }
    }

    fun addNextPage(MODE: Int, movieName:String ="") {
        if (MODE == MODE_MAIN) {
            mainPage += 1
            getMovieList(mainPage)
        } else if (MODE == MODE_SEARCH) {
            searchPage += 1
            searchMovie(movieName, searchPage)
        }
    }


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(application) as T
        }
    }
}