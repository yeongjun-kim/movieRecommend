package com.mvvm.movierecommend.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "FavoriteMovieItem")
data class FavoriteMovieItem(
    @PrimaryKey
    var id: String = "",
    val title: String,
    val overview: String,
    val genre_ids: String,
    val poster_path: String?,
    val adult: Boolean,
    val release_date: String,
    val popularity: Double,
    val vote_average: Double,
    val vote_count: Long
)