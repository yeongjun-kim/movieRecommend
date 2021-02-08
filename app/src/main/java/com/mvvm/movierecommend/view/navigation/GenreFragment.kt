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
import com.mvvm.movierecommend.databinding.FragmentGenreBinding
import com.mvvm.movierecommend.view.MainActivity.Companion.MODE_GENRE
import com.mvvm.movierecommend.view.adapter.ItemOffsetDecoration
import com.mvvm.movierecommend.view.adapter.RvAdapter
import com.mvvm.movierecommend.viewModel.MainViewModel


class GenreFragment : Fragment() {

    lateinit var binding: FragmentGenreBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var rv: RecyclerView
    var rvAdapter = RvAdapter()

    var genreList = arrayListOf<String>()
    var sortBy = "popularity.desc"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_genre, container, false)
        binding.apply {
            fm = this@GenreFragment
            lifecycleOwner = this@GenreFragment
        }
        rv = binding.fmGenreRv
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModel.Factory(activity!!.application))
                .get(MainViewModel::class.java)

        mainViewModel.genreLiveMovieList.observe(this, Observer { newList ->
            rvAdapter.setList(newList)
        })

        initRv()
    }


    fun initRv() {
        rvAdapter.listener = object : RvAdapter.ClickListener {
            override fun onClick(position: Int) {
                mainViewModel.setdetailItem(rvAdapter.movieList[position])

                activity?.supportFragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    ?.replace(R.id.main_fl, DetailFragment())?.addToBackStack("genreFragment")
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

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter!!.itemCount

                if (lastPosition == totalCount - 1) { // GirdLayout이라 totalCount -1 해줘야함.
                    mainViewModel.addNextPage(MODE_GENRE)
                }
            }
        })
    }

    fun getGenreMovie() {
        mainViewModel.genreGenre = genreList.joinToString(",")
        mainViewModel.sortBy = sortBy
        mainViewModel.getGenreMovieList(true)
    }

    fun setGenre(num: Int) {
        if (genreList.contains(num.toString())) genreList.remove(num.toString())
        else genreList.add(num.toString())
    }

    fun setSort(insertString: String) {
        sortBy = insertString
    }

    fun isGenreVisibility() {
        if (binding.fmClGenre.visibility == View.VISIBLE) binding.fmClGenre.visibility = View.GONE
        else binding.fmClGenre.visibility = View.VISIBLE
    }

    fun isSortVisibility() {
        if (binding.fmClSort.visibility == View.VISIBLE) binding.fmClSort.visibility = View.GONE
        else binding.fmClSort.visibility = View.VISIBLE
    }


}

