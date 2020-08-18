package com.mvvm.movierecommend.view.adapter

import android.annotation.SuppressLint
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.api.BASE_URL
import com.mvvm.movierecommend.api.BASE_URL_IMAGE
import com.mvvm.movierecommend.databinding.ItemRvBinding
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

    interface ClickListener {
        fun onClick(position: Int)
    }


    var listener: ClickListener? = null
    var movieList = arrayListOf<MovieItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rv,
                parent,
                false
            ), listener
        )
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

    class CustomViewHolder(val binding: ItemRvBinding, val listener: ClickListener?) :RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { listener?.onClick(adapterPosition) }
        }

        fun bind(item: MovieItem) {
            binding.apply {
                itemRvTvTitle.isSelected = true
                movieItem = item
            }
        }
    }
}