package com.metehan.mobilliumandroidchallange.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.metehan.mobilliumandroidchallange.R
import com.metehan.mobilliumandroidchallange.adapters.NpMoviesAdapter
import com.metehan.mobilliumandroidchallange.adapters.ViewPagerAdapter
import com.metehan.mobilliumandroidchallange.databinding.ActivityMainScreenBinding
import com.metehan.mobilliumandroidchallange.util.Constants.Companion.BASE_IMAGE_URL
import com.metehan.mobilliumandroidchallange.util.GenerateAlertDialog
import com.metehan.mobilliumandroidchallange.util.NetworkListener
import com.metehan.mobilliumandroidchallange.util.NetworkResult
import com.metehan.mobilliumandroidchallange.viewmodels.MainViewModel
import com.metehan.mobilliumandroidchallange.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var networkListener: NetworkListener
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesViewModel: MoviesViewModel
    private val mAdapter by lazy { NpMoviesAdapter() }

    private var titleList = mutableListOf<String>()
    private var overviewList = mutableListOf<String>()
    private var imageUrlList = mutableListOf<String>()

    private lateinit var mProgressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        moviesViewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("Please wait...")
        mProgressDialog.show()
        setupRecyclerView()
        moviesViewModel.readBackOnline.observe(this@MainScreenActivity) {
            moviesViewModel.backOnline = it
        }
        networkListener = NetworkListener()
        lifecycleScope.launch {
            networkListener.checkNetworkAvailability(this@MainScreenActivity)
                .collect { status ->
                    moviesViewModel.networkStatus = status
                    moviesViewModel.showNetworkStatus()
                    readUpcomingMovies()
                    readNowPlayingMovie()
                }
        }

    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener { clickedMovie ->
            val intent = Intent(this@MainScreenActivity, DetailScreenActivity::class.java)
            intent.putExtra("movie_id", clickedMovie.id)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainScreenActivity)
        showShimmerEffect()
    }

    private fun readNowPlayingMovie() {
        lifecycleScope.launch {
            requestNowPlayingMovie()
        }
    }

    private fun readUpcomingMovies() {
        lifecycleScope.launch {
            requestUpcomingMovies()
        }
    }

    private fun addToList(title: String, overview: String, imageUrl: String) {
        titleList.add(title)
        overviewList.add(overview)
        imageUrlList.add(imageUrl)
    }

    private fun requestNowPlayingMovie() {
        mainViewModel.getNowPlayingMovies(moviesViewModel.applyQueries())

        mainViewModel.nowPlayingMoviesResponse.observe(this@MainScreenActivity) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    mProgressDialog.dismiss()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
//                    loadDataFromCache()
                    mProgressDialog.dismiss()
                    GenerateAlertDialog.showAlertDialogFragment(
                        this@MainScreenActivity,
                        R.drawable.ic_no_connection,
                        response.message.toString(),
                        3000
                    )

                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                else -> {}
            }
        }
    }

    private fun requestUpcomingMovies() {
        mainViewModel.getUpcomingMovies(moviesViewModel.applyQueries())

        mainViewModel.upcomingMoviesResponse.observe(this@MainScreenActivity) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    mProgressDialog.dismiss()
                    hideShimmerEffect()
                    response.data!!.results.forEach {
                        addToList(
                            it.title + " (" + it.year + ")",
                            it.overview,
                            BASE_IMAGE_URL + it.poster_path
                        )
                    }
                    binding.viewPager2.adapter =
                        ViewPagerAdapter(this, titleList, overviewList, imageUrlList)
                    binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                    binding.indicator.setViewPager(binding.viewPager2)
                }

                is NetworkResult.Error -> {
                    mProgressDialog.dismiss()
                    hideShimmerEffect()
                    GenerateAlertDialog.showAlertDialogFragment(
                        this@MainScreenActivity,
                        R.drawable.ic_no_connection,
                        response.message.toString(),
                        3000
                    )
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                else -> {}
            }
        }
    }


    private fun showShimmerEffect() {
        binding.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerView.hideShimmer()
    }

}