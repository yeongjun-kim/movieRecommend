package com.mvvm.movierecommend.view.adapter

import android.annotation.SuppressLint
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.api.BASE_URL
import com.mvvm.movierecommend.api.BASE_URL_IMAGE
import com.mvvm.movierecommend.model.MovieItem
import com.mvvm.movierecommend.view.MainActivity.Companion.genreIdToString
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_rv.view.*
import java.util.*
import kotlin.collections.ArrayList

class RvAdapter : RecyclerView.Adapter<RvAdapter.CustomViewHolder>() {

    interface ClickListener{
        fun onClick(position:Int)
    }


    var listener:ClickListener? = null
    var movieList = arrayListOf<MovieItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv, null)
        return CustomViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = movieList[position]
        val customViewHolder = holder as CustomViewHolder
        customViewHolder.bind(item)
    }

    override fun getItemCount() = movieList.size


    @SuppressLint("CheckResult")
    fun setList(newList: ArrayList<MovieItem>) {
        Observable.just(newList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { DiffUtil.calculateDiff(RvDiffCallback(movieList, newList)) }
            .subscribe {
                movieList = newList
                it.dispatchUpdatesTo(this)
                notifyDataSetChanged()
            }
        movieList = newList


    }

    class CustomViewHolder(view: View, val listener:ClickListener?) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener { listener?.onClick(adapterPosition) }
        }

        val iv = view.item_rv_iv
        val tv_title = view.item_rv_tv_title
        val tv_genre = view.item_rv_tv_genre
        val rb = view.item_rv_rb
        val tv_overview = view.item_rv_tv_overview

        init {
            tv_title.isSelected = true // 글씨 초과시 흐름 효과 적용
        }

        fun bind(item: MovieItem) {
            tv_title.text = item.title
            tv_genre.text = item.genre_ids.map { id -> genreIdToString[id]}.joinToString(",")
            rb.rating = (item.vote_average/2).toFloat()
            tv_overview.text = item.overview
            Glide.with(itemView.context).load("${BASE_URL_IMAGE}${item.poster_path}")
                .thumbnail(0.1f)
                .placeholder(R.drawable.icon_no_image)
                .apply(RequestOptions().circleCrop())
                .into(iv)

        }
    }


}