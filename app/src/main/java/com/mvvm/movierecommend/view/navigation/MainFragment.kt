package com.mvvm.movierecommend.view.navigation

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
import com.mvvm.movierecommend.view.MainActivity.Companion.MODE_MAIN
import com.mvvm.movierecommend.view.adapter.ItemOffsetDecoration
import com.mvvm.movierecommend.view.adapter.RvAdapter
import com.mvvm.movierecommend.viewModel.MainViewModel


class MainFragment : Fragment() {


    lateinit var mainViewModel: MainViewModel
    lateinit var rv: RecyclerView
    var rvAdapter = RvAdapter()
    var isFirst = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.mvvm.movierecommend.R.layout.fragment_main, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(com.mvvm.movierecommend.R.id.fm_main_rv)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel =
            ViewModelProvider(requireActivity(),MainViewModel.Factory(activity!!.application))
                .get(MainViewModel::class.java)

        mainViewModel.mainLiveMovieList.observe(this, Observer { newList ->
            rvAdapter.setList(newList)
        })
        initRv()
    }


    private fun initRv() {
        rvAdapter.listener = object : RvAdapter.ClickListener {
            override fun onClick(position: Int) {
                mainViewModel.detailItem = rvAdapter.movieList[position]
                activity?.supportFragmentManager?.beginTransaction()
                    ?.setCustomAnimations(
                        com.mvvm.movierecommend.R.anim.fade_in,
                        com.mvvm.movierecommend.R.anim.fade_out
                    )
                    ?.replace(com.mvvm.movierecommend.R.id.main_fl, DetailFragment())
                    ?.addToBackStack("mainFragment")
                    ?.commit()
            }
        }

        rv.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            addItemDecoration(
                ItemOffsetDecoration(
                    context,
                    com.mvvm.movierecommend.R.dimen.item_offset
                )
            ) // GridLayout에서 Item간 Spacing 해주기 위함
            if(isFirst) mainViewModel.getMovieList()    // 처음 생성될때만 1페이지 가져오기
            isFirst = false
            adapter = rvAdapter
        }

        /**
         * Listen last position for paging
         */
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter!!.itemCount

                if (lastPosition == totalCount - 1) { // GirdLayout이라 totalCount -1 해줘야함.
                    mainViewModel.addNextPage(MODE_MAIN)
                }
            }
        })
    }
}
