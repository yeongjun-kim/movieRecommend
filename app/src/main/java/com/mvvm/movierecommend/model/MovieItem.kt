package com.mvvm.movierecommend.model

/**
 * id : 고유 ID
 * title : 타이틀
 * overview : 설명
 * genre_ids : 장르(리스트)
 * poster_path : 포스터 이미지 경로
 * adult : 성인
 * release_date : 출시일
 * popularity : 인기도
 * vote_average : 평점
 * vote_count : 투표수
 */
class MovieItem(
    val id: String,
    val title: String,
    val overview: String,
    val genre_ids: List<String>,
    val poster_path: String,
    val adult: Boolean,
    val release_date: String,
    val popularity: Double,
    val vote_average: Double,
    val vote_count: Long
)