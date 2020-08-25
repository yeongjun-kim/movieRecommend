package com.mvvm.movierecommend.view.navigation

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.databinding.FragmentDetailBinding
import com.mvvm.movierecommend.viewModel.MainViewModel

class DetailFragment : Fragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var binding: FragmentDetailBinding
    var animator = ValueAnimator()
    var toggle = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)


        binding.fmDetailLaLike.setOnClickListener {
            onClickLike()
        }



        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModel.Factory(activity!!.application)).get(
                MainViewModel::class.java
            )
        binding.apply {
            lifecycleOwner = this@DetailFragment
            viewModel = mainViewModel
            fm = this@DetailFragment
        }

        mainViewModel.getAll().observe(this, Observer {
            if(it.contains(mainViewModel.detailITemToFavoriteMovieItem)){ // 좋아요 눌려있다면

            }
            else{ //좋아요 안눌려있다면

            }
        })
    }

    fun finish() {
        activity?.supportFragmentManager?.popBackStack()
    }

    fun onClickLike(){
        animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000)
        animator.addUpdateListener { animation->
            binding.fmDetailLaLike.progress = animation.animatedValue as Float
        }
        animator.start()
        mainViewModel.insert()
//        mainViewModel.delete()
    }
}