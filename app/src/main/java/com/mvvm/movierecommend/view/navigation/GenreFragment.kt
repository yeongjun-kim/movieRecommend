package com.mvvm.movierecommend.view.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.databinding.FragmentGenreBinding
import com.mvvm.movierecommend.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_genre.*


class GenreFragment : Fragment() {

    lateinit var binding: FragmentGenreBinding
    lateinit var mainViewModel: MainViewModel
    var genreList = arrayListOf<Int>()
    var sortBy = "popularity.desc"


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_genre, container, false)
        binding.apply {
            fm = this@GenreFragment
            lifecycleOwner = this@GenreFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel =ViewModelProvider(requireActivity(), MainViewModel.Factory(activity!!.application))
            .get(MainViewModel::class.java)
    }

    fun setGenre(num:Int){
        if(genreList.contains(num)) genreList.remove(num)
        else genreList.add(num)
    }

    fun setSort(insertString:String){
        sortBy = insertString
    }


}

