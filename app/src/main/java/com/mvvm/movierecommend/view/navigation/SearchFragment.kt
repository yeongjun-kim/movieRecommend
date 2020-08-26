package com.mvvm.movierecommend.view.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.view.MainActivity
import com.mvvm.movierecommend.view.MainActivity.Companion.MODE_SEARCH
import com.mvvm.movierecommend.view.adapter.ItemOffsetDecoration
import com.mvvm.movierecommend.view.adapter.RvAdapter
import com.mvvm.movierecommend.viewModel.MainViewModel

class SearchFragment : Fragment() {

    lateinit var binding: com.mvvm.movierecommend.databinding.FragmentSearchBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var rv: RecyclerView
    var rvAdapter = RvAdapter()
    var searchMovieName: MutableLiveData<String> = MutableLiveData("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.apply {
            fm = this@SearchFragment
            lifecycleOwner = this@SearchFragment
        }
        rv = binding.fmSearchRv
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel =
            ViewModelProvider(
                requireActivity(),
                MainViewModel.Factory(activity!!.application)
            ).get(
                MainViewModel::class.java
            )
        mainViewModel.searchLiveMovieList.observe(this, Observer { newList ->
            rvAdapter.setList(newList)
        })
        initRv()
    }


    fun searchMovie() {
        mainViewModel.searchMovie(searchMovieName.value.toString())
    }

    fun initRv() {
        rvAdapter.listener = object : RvAdapter.ClickListener {
            override fun onClick(position: Int) {
                mainViewModel.setdetailItem(rvAdapter.movieList[position])
                activity?.supportFragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    ?.replace(R.id.main_fl, DetailFragment())?.addToBackStack("searchFragment")
                    ?.commit()
            }
        }

        rv.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            addItemDecoration(
                ItemOffsetDecoration(
                    context,
                    R.dimen.item_offset
                )
            ) // GridLayout에서 Item간 Spacing 해주기 위함
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
                    mainViewModel.addNextPage(MODE_SEARCH, searchMovieName.value.toString())
                }
            }
        })

    }


}
