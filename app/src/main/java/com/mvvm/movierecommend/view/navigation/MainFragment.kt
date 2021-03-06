package com.mvvm.movierecommend.view.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.view.MainActivity.Companion.MODE_MAIN
import com.mvvm.movierecommend.view.adapter.ItemOffsetDecoration
import com.mvvm.movierecommend.view.adapter.RvAdapter
import com.mvvm.movierecommend.viewModel.MainViewModel


class MainFragment : Fragment() {


    lateinit var mainViewModel: MainViewModel
    lateinit var binding: com.mvvm.movierecommend.databinding.FragmentMainBinding
    var rvAdapter = RvAdapter()
    var isFirst = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModel.Factory(activity!!.application))
                .get(MainViewModel::class.java)

        mainViewModel.mainLiveMovieList.observe(this, Observer { newList ->
            rvAdapter.setList(newList)
        })
        initRv()
    }


    private fun initRv() {
        rvAdapter.listener = object : RvAdapter.ClickListener {
            override fun onClick(position: Int) {
                mainViewModel.setdetailItem(rvAdapter.movieList[position])
                activity?.supportFragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    ?.replace(R.id.main_fl, DetailFragment())
                    ?.addToBackStack("mainFragment")
                    ?.commit()
            }
        }

        binding.fmMainRv.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            addItemDecoration(
                ItemOffsetDecoration(
                    context,
                    com.mvvm.movierecommend.R.dimen.item_offset
                )
            ) // GridLayout에서 Item간 Spacing 해주기 위함
            if (isFirst) mainViewModel.getMovieList()    // 처음 생성될때만 1페이지 가져오기
            isFirst = false
            adapter = rvAdapter
        }

        /**
         * Listen last position for paging
         */
        binding.fmMainRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
