package com.mvvm.movierecommend.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mvvm.movierecommend.model.MovieItem

class RvDiffCallback(val oldList: ArrayList<MovieItem>, val newList: ArrayList<MovieItem>) :
    DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].equals(newList[newItemPosition])
    }

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
}