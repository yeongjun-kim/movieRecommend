package com.mvvm.movierecommend.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favoritemovieitem")
    fun getAll(): LiveData<MutableList<FavoriteMovieItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteMovieItem: FavoriteMovieItem)

    @Delete
    fun delete(favoriteMovieItem: FavoriteMovieItem)
}