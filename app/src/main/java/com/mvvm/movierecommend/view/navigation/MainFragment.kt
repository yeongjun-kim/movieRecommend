package com.mvvm.movierecommend.view.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.model.MovieItem
import com.mvvm.movierecommend.view.adapter.ItemOffsetDecoration
import com.mvvm.movierecommend.view.adapter.RvAdapter
import com.mvvm.movierecommend.viewModel.MainViewModel


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
        mainViewModel = ViewModelProvider(requireActivity(),MainViewModel.Factory(activity!!.application)).get(MainViewModel::class.java)
        mainViewModel.liveMovieList.observe(this, Observer { newList ->
            rvAdapter.setList(newList)
        })

        initRv()

    }



    private fun initRv() {
        rvAdapter.listener = object : RvAdapter.ClickListener {
            override fun onClick(position: Int) {
                mainViewModel.detailItem = rvAdapter.movieList[position]
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_fl, DetailFragment())?.commit()
            }
        }

        rv.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            addItemDecoration(ItemOffsetDecoration(context,R.dimen.item_offset)) // GridLayout에서 Item간 Spacing 해주기 위함
            mainViewModel.getMovieList()
            adapter = rvAdapter
        }
    }


}
