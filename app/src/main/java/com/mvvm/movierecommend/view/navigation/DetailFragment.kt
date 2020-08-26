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
    var isConstain = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

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
            var temp = it.find { a -> a.id == mainViewModel.detailITemToFavoriteMovieItem?.id }
            isConstain = temp != null

            if (isConstain) setLottie(0.5f)
            else setLottie(0.0f)
        })
    }

    fun finish() {
        activity?.supportFragmentManager?.popBackStack()
    }

    fun setLottie(startEnd: Float) {
        animator = ValueAnimator.ofFloat(startEnd, startEnd).setDuration(0)
        animator.addUpdateListener { animation ->
            binding.fmDetailLaLike.progress = animation.animatedValue as Float
        }
        animator.start()
    }

    fun onClickLike() {
        var start = if (isConstain) 0.5f else 0f
        var end = if (isConstain) 1f else 0.5f

        animator = ValueAnimator.ofFloat(start, end).setDuration(1000)
        animator.addUpdateListener { animation ->
            binding.fmDetailLaLike.progress = animation.animatedValue as Float
        }
        animator.start()

        if (isConstain) mainViewModel.delete()
        else mainViewModel.insert()
    }
}