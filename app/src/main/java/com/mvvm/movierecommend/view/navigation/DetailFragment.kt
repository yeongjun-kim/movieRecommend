package com.mvvm.movierecommend.view.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.databinding.FragmentDetailBinding
import com.mvvm.movierecommend.viewModel.MainViewModel

class DetailFragment : Fragment() {

    lateinit var mainViewModel:MainViewModel
    lateinit var binding:FragmentDetailBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_detail,null)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity(),MainViewModel.Factory(activity!!.application)).get(MainViewModel::class.java)
        binding.apply {
            lifecycleOwner = this@DetailFragment
            viewModel = mainViewModel
            fm = this@DetailFragment
        }
        Log.d("fhrm", "DetailFragment -onActivityCreated(),    : ${mainViewModel.detailItem?.adult}")
    }



    override fun onDestroy() {
        super.onDestroy()
        fragmentManager?.popBackStack()
    }

}