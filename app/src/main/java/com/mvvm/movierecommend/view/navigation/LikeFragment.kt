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
import com.mvvm.movierecommend.databinding.FragmentLikeBinding
import com.mvvm.movierecommend.view.MainActivity
import com.mvvm.movierecommend.view.adapter.ItemOffsetDecoration
import com.mvvm.movierecommend.view.adapter.RvAdapter
import com.mvvm.movierecommend.viewModel.MainViewModel

class LikeFragment : Fragment() {


    lateinit var binding: FragmentLikeBinding
    lateinit var mainViewModel: MainViewModel
    var rvAdapter = RvAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_like, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModel.Factory(activity!!.application))
                .get(MainViewModel::class.java)

        mainViewModel.getAll().observe(this, Observer { newList ->
            var conVertList =
                newList.map { item -> mainViewModel.favoriteMovieItemToMovieItem(item) }
            rvAdapter.setList(ArrayList(conVertList))
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
                    ?.addToBackStack("likeFragment")
                    ?.commit()
            }
        }

        binding.fmLikeRv.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            addItemDecoration(
                ItemOffsetDecoration(
                    context,
                    R.dimen.item_offset
                )
            ) // GridLayout에서 Item간 Spacing 해주기 위함
            mainViewModel.getMovieList()    // 처음 생성될때만 1페이지 가져오기
            adapter = rvAdapter
        }

    }
}