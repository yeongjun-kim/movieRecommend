package com.mvvm.movierecommend.view.adapter

import android.media.Rating
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.api.BASE_URL_IMAGE
import com.mvvm.movierecommend.view.MainActivity.Companion.genreIdToString

object BindingAdapters {
    /**
     * 별점(Double -> Float)
     */
    @JvmStatic
    @BindingAdapter("setRating")
    fun setRating(ratingBar: RatingBar, double:Double){
        ratingBar.rating = (double/2).toFloat()
    }

    /**
     * 장르id(list) -> 한글장르(list)
     */
    @JvmStatic
    @BindingAdapter("setGenre")
    fun setGenre(textView:TextView, id:List<String>){
        textView.text = id.map { id -> genreIdToString[id] }.joinToString(",")
    }

    /**
     * ImageView
     */
    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(imageView:ImageView, url:String){
        Glide.with(imageView.context).load("${BASE_URL_IMAGE}${url}")
            .thumbnail(0.1f)
            .placeholder(R.drawable.icon_no_image)
//            .apply(RequestOptions().circleCrop())
            .into(imageView)
    }

    /**
     * Set Adult Icon
     */
    @JvmStatic
    @BindingAdapter("setAdult")
    fun setAdult(imageView:ImageView, isAdult:Boolean){
        if(isAdult) imageView.visibility = View.VISIBLE else imageView.visibility = View.GONE
    }
}