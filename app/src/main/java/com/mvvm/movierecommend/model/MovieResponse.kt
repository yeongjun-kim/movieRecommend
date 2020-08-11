package com.mvvm.movierecommend.model

class MovieResponse(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: ArrayList<MovieItem>
)