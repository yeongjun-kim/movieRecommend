package com.mvvm.movierecommend.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Movie
import android.util.Log
import androidx.lifecycle.*
import com.mvvm.movierecommend.api.MovieApi.getMovieList
import com.mvvm.movierecommend.model.FavoriteMovieItem
import com.mvvm.movierecommend.model.MovieItem
import com.mvvm.movierecommend.repository.FavoriteMovieRepository
import com.mvvm.movierecommend.repository.MovieRepository
import com.mvvm.movierecommend.view.MainActivity.Companion.MODE_GENRE
import com.mvvm.movierecommend.view.MainActivity.Companion.MODE_MAIN
import com.mvvm.movierecommend.view.MainActivity.Companion.MODE_SEARCH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * mFavoriteMovieRepository: DetailFragment 에서 좋아요 누른 FavoriteMovieItem 저장에 이용할 Repository
     * mainLiveMovieList: MainFragment 에서 observe 할 list
     * searchLiveMovieList: SearchFragment 에서 observe 할 list
     * genreLiveMovieList: genreFragment 에서 observe 할 list
     * movieRepository: Repository
     * detailItem: Fragment 에서 item click 시 detailFragment 에 전달할 item
     * mainPage: MainFragment 의 페이지
     * searchPage: SearchFragmnet 의 페이지
     * genrePage: SearchFragmnet 의 페이지
     * beforeMovieName: 다른 영화명을 검색할시 searchMovieList를 초기화 해주기 위한 임시 property
     */

    private var mFavoriteMovieRepository: FavoriteMovieRepository
    private var mFavoriteMovieList: LiveData<MutableList<FavoriteMovieItem>>

    var mainLiveMovieList: MutableLiveData<ArrayList<MovieItem>> = MutableLiveData()
    var searchLiveMovieList: MutableLiveData<ArrayList<MovieItem>> = MutableLiveData()
    var genreLiveMovieList: MutableLiveData<ArrayList<MovieItem>> = MutableLiveData()

    var mainMovieList: ArrayList<MovieItem> = arrayListOf()
    var searchMovieList: ArrayList<MovieItem> = arrayListOf()
    var genreMovieList: ArrayList<MovieItem> = arrayListOf()

    var mainPage = 1
    var searchPage = 1
    var genrePage = 1
    var beforeMovieName = ""
    var genreGenre: String? = null
    var sortBy: String = "popularity.desc"

    var movieRepository = MovieRepository()
    var detailItem: MovieItem? = null
    var detailITemToFavoriteMovieItem: FavoriteMovieItem? = null

    init {
        mFavoriteMovieRepository = FavoriteMovieRepository(application)
        mFavoriteMovieList = mFavoriteMovieRepository.getAll()

    }


    fun setdetailItem(inputMovieItem: MovieItem) {
        detailItem = inputMovieItem
        detailITemToFavoriteMovieItem = movieItemToFavoriteMovieItem(inputMovieItem)
    }

    fun movieItemToFavoriteMovieItem(input: MovieItem): FavoriteMovieItem {
        return FavoriteMovieItem(
            input.id,
            input.title,
            input.overview,
            input.genre_ids.joinToString(","),
            input.poster_path,
            input.adult,
            input.release_date,
            input.popularity,
            input.vote_average,
            input.vote_count
        )
    }

    fun favoriteMovieItemToMovieItem(input: FavoriteMovieItem): MovieItem {
        return MovieItem(
            input.id,
            input.title,
            input.overview,
            input.genre_ids.split(','),
            input.poster_path,
            input.adult,
            input.release_date,
            input.popularity,
            input.vote_average,
            input.vote_count
        )
    }


    @SuppressLint("CheckResult")
    fun getMovieList(page: Int = 1) {
        movieRepository.getMovieList(page)
            .subscribe({ movieResponse ->
                setList(MODE_MAIN, movieResponse.results)
            }, { e ->
                Log.d("fhrm", "MainViewModel -getMovieList(),    : ${e.message}")
            })
    }

    @SuppressLint("CheckResult")
    fun getGenreMovieList(callFromFragment: Boolean = false, page: Int = 1) {
        if (callFromFragment) {
            genreLiveMovieList.value?.clear()
            genreMovieList.clear()
            genrePage = 1
        }

        movieRepository.getGenreMovieList(page, genreGenre, sortBy)
            .subscribe({ movieResponse ->
                setList(MODE_GENRE, movieResponse.results)
            }, { e ->
                Log.d("fhrm", "MainViewModel -getMovieList(),    : ${e.message}")
            })
    }

    @SuppressLint("CheckResult")
    fun searchMovie(movieName: String, page: Int = 1) {
        // 다른 영화명 검색시 이전 history clear
        if (beforeMovieName != movieName) {
            searchLiveMovieList.value?.clear()
            searchMovieList.clear()
            searchPage = 1
        }

        beforeMovieName = movieName
        movieRepository.searchMovie(movieName, page)
            .subscribe({ movieResponse ->
                setList(MODE_SEARCH, movieResponse.results)
            }, { e ->
                Log.d("fhrm", "MainViewModel -searchMovie(),    : ${e.message}")
            })
    }

    private fun setList(MODE: Int, newList: ArrayList<MovieItem>) {
        if (MODE == MODE_MAIN) {
            mainMovieList.addAll(newList)
            mainLiveMovieList.value = mainMovieList
        } else if (MODE == MODE_SEARCH) {
            searchMovieList.addAll(newList)
            searchLiveMovieList.value = searchMovieList
        } else if (MODE == MODE_GENRE) {
            genreMovieList.addAll(newList)
            genreLiveMovieList.value = genreMovieList
        }
    }

    fun addNextPage(MODE: Int, movieName: String = "") {
        if (MODE == MODE_MAIN) {
            mainPage += 1
            getMovieList(mainPage)
        } else if (MODE == MODE_SEARCH) {
            searchPage += 1
            searchMovie(movieName, searchPage)
        } else if (MODE == MODE_GENRE) {
            genrePage += 1
            getGenreMovieList(false, genrePage)
        }
    }

    fun getAll() = mFavoriteMovieList

    fun insert() {
        viewModelScope.launch(Dispatchers.IO) {
            mFavoriteMovieRepository.insert(movieItemToFavoriteMovieItem(detailItem!!))
        }
    }

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            mFavoriteMovieRepository.delete(movieItemToFavoriteMovieItem(detailItem!!))
        }
    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(application) as T
        }
    }
}