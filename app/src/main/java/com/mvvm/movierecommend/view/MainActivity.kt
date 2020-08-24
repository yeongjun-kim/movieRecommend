package com.mvvm.movierecommend.view

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.repository.MovieRepository
import com.mvvm.movierecommend.view.navigation.GenreFragment
import com.mvvm.movierecommend.view.navigation.LikeFragment
import com.mvvm.movierecommend.view.navigation.MainFragment
import com.mvvm.movierecommend.view.navigation.SearchFragment
import com.mvvm.movierecommend.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    var mainFragment: Fragment = MainFragment()
    var searchFragment: Fragment = SearchFragment()
    var genreFragment: Fragment = GenreFragment()
    var likeFragment: Fragment = LikeFragment()

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, mainFragment)
//                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, searchFragment)
//                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_genre -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, genreFragment)
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_like -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, likeFragment)
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        initDefaultFragment()

        val mainViewModel = ViewModelProvider(this,
            MainViewModel.Factory(application)).get(MainViewModel::class.java)

    }

    private fun initDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fl, mainFragment)
            .commit()

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main_fl, mainFragment)
//            .addToBackStack(null)
//            .commit()
    }


    companion object{
        val MODE_MAIN = 0
        val MODE_SEARCH = 1
        val genreIdToString:Map<String,String> = mapOf("28" to "액션",
            "12" to "모험",
            "16" to "애니메이션",
            "35" to "코미디",
            "80" to "범죄",
            "99" to "다큐멘터리",
            "18" to "드라마",
            "10751" to "가족",
            "14" to "판타지",
            "36" to "역사",
            "27" to "공포",
            "10402" to "음악",
            "9648" to "미스터리",
            "10749" to "로맨스",
            "878" to "SF",
            "10770" to "TV to 영화",
            "53" to "스릴러",
            "10752" to "전쟁",
            "37" to "서부")
    }
}


