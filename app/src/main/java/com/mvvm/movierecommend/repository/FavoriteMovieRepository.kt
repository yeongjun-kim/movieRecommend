package com.mvvm.movierecommend.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.mvvm.movierecommend.model.FavoriteMovieDao
import com.mvvm.movierecommend.model.FavoriteMovieDatabase
import com.mvvm.movierecommend.model.FavoriteMovieItem

class FavoriteMovieRepository(application: Application) {

    private var mFavoriteMovieDatabase: FavoriteMovieDatabase
    private var mFavoriteMovieDao: FavoriteMovieDao
    private var mFavoriteMovieList: LiveData<MutableList<FavoriteMovieItem>>

    init {
        mFavoriteMovieDatabase = FavoriteMovieDatabase.getInstance(application)
        mFavoriteMovieDao = mFavoriteMovieDatabase.favoriteMovieDao()
        mFavoriteMovieList = mFavoriteMovieDao.getAll()
    }

    fun getAll(): LiveData<MutableList<FavoriteMovieItem>> = mFavoriteMovieList

    fun insert(item: FavoriteMovieItem) {
        mFavoriteMovieDao.insert(item)
    }

    fun delete(item: FavoriteMovieItem) {
        mFavoriteMovieDao.delete(item)
    }


}