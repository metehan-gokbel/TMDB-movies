package com.metehan.mobilliumandroidchallange.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.metehan.mobilliumandroidchallange.R
import com.metehan.mobilliumandroidchallange.adapters.NpMoviesAdapter
import com.metehan.mobilliumandroidchallange.databinding.ActivityMainScreenBinding
import com.metehan.mobilliumandroidchallange.databinding.ActivityPopularMoviesBinding
import com.metehan.mobilliumandroidchallange.util.GenerateAlertDialog
import com.metehan.mobilliumandroidchallange.util.NetworkListener
import com.metehan.mobilliumandroidchallange.util.NetworkResult
import com.metehan.mobilliumandroidchallange.viewmodels.MainViewModel
import com.metehan.mobilliumandroidchallange.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularMoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopularMoviesBinding
    private lateinit var networkListener: NetworkListener
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesViewModel: MoviesViewModel
    private val mAdapter by lazy { NpMoviesAdapter() }
    private lateinit var mProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        moviesViewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("Please wait...")
        mProgressDialog.show()
        binding.backButton.setOnClickListener {
            val intent = Intent(this@PopularMoviesActivity, MainScreenActivity::class.java)
            intent.flags =  Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        setupRecyclerView()
        moviesViewModel.readBackOnline.observe(this@PopularMoviesActivity) {
            moviesViewModel.backOnline = it
        }
        networkListener = NetworkListener()
        lifecycleScope.launch {
            networkListener.checkNetworkAvailability(this@PopularMoviesActivity)
                .collect { status ->
                    moviesViewModel.networkStatus = status
                    moviesViewModel.showNetworkStatus()
                    readUpcomingMovies()
                }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewPopular.adapter = mAdapter
        binding.recyclerViewPopular.layoutManager = LinearLayoutManager(this@PopularMoviesActivity)
        showShimmerEffect()
    }

    private fun readUpcomingMovies() {
        lifecycleScope.launch {
            requestApiData()
        }
    }

    private fun requestApiData() {
        mainViewModel.getPopularMovies(moviesViewModel.applyQueries())

        mainViewModel.popularMoviesResponse.observe(this@PopularMoviesActivity) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    mProgressDialog.dismiss()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    mProgressDialog.dismiss()
                    GenerateAlertDialog.showAlertDialogFragment(
                        this@PopularMoviesActivity,
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
        binding.recyclerViewPopular.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerViewPopular.hideShimmer()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@PopularMoviesActivity, MainScreenActivity::class.java)
        intent.flags =  Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}