package com.mvvm.movierecommend.view

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mvvm.movierecommend.R
import com.mvvm.movierecommend.repository.MovieRepository
import com.mvvm.movierecommend.view.navigation.GenreFragment
import com.mvvm.movierecommend.view.navigation.LikeFragment
import com.mvvm.movierecommend.view.navigation.MainFragment
import com.mvvm.movierecommend.view.navigation.SearchFragment

class MainActivity : AppCompatActivity() {

    val TAG: String = "fhrm"
    var mainFragment: Fragment = MainFragment()
    var searchFragment: Fragment = SearchFragment()
    var genreFragment: Fragment = GenreFragment()
    var likeFragment: Fragment = LikeFragment()

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, mainFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, searchFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_genre -> {
                    supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, genreFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_like -> {
                    supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, likeFragment)
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

        MovieRepository().getMovieList()
            .subscribe { Log.d(TAG, "MainActivity -onCreate(),    : ${it.results[0].title}") }

    }

    private fun initDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fl, mainFragment)
            .commit()
    }
}


