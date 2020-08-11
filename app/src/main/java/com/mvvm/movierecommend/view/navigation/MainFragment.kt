package com.mvvm.movierecommend.view.navigation

import android.app.Application
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.view.adapter.RvAdapter
import com.mvvm.movierecommend.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*


val TAG: String = "fhrm"

class MainFragment : Fragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var rv: RecyclerView
    var rvAdapter = RvAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.fm_main_rv)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = ViewModelProvider(
            this,
            MainViewModel.Factory(activity!!.application)
        ).get(MainViewModel::class.java)
        mainViewModel.liveMovieList.observe(this, Observer { newList ->
            rvAdapter.setList(newList)
        })

        initRv()

    }

    private fun initRv() {
        rv.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            mainViewModel.getMovieList()
            adapter = rvAdapter
        }
    }


}
