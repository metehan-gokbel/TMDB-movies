package com.metehan.mobilliumandroidchallange.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.metehan.mobilliumandroidchallange.R
import com.metehan.mobilliumandroidchallange.databinding.ActivityDetailScreenBinding
import com.metehan.mobilliumandroidchallange.util.Constants

import com.metehan.mobilliumandroidchallange.util.Constants.Companion.API_KEY
import com.metehan.mobilliumandroidchallange.util.Constants.Companion.BASE_IMAGE_URL
import com.metehan.mobilliumandroidchallange.util.NetworkListener
import com.metehan.mobilliumandroidchallange.util.NetworkResult
import com.metehan.mobilliumandroidchallange.util.observeOnce
import com.metehan.mobilliumandroidchallange.viewmodels.MainViewModel
import com.metehan.mobilliumandroidchallange.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailScreenBinding
    private lateinit var networkListener: NetworkListener
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesViewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        moviesViewModel = ViewModelProvider(this)[MoviesViewModel::class.java]

        binding.buttonDiscover.setOnClickListener {
            val intent = Intent(this@DetailScreenActivity, PopularMoviesActivity::class.java)
            startActivity(intent)
        }
        moviesViewModel.readBackOnline.observe(this@DetailScreenActivity) {
            moviesViewModel.backOnline = it
        }

        binding.backImageButton.setOnClickListener {
            finish()
        }

        networkListener = NetworkListener()
        val movieId = intent.getIntExtra("movie_id", 0)
        lifecycleScope.launch {
            networkListener.checkNetworkAvailability(this@DetailScreenActivity)
                .collect { status ->
                    moviesViewModel.networkStatus = status
                    moviesViewModel.showNetworkStatus()
                    requestApiData(movieId, API_KEY)
                }
        }
    }

    private fun requestApiData(movieId: Int, apiKey: String) {
        mainViewModel.getMovieDetails(movieId, apiKey)

        mainViewModel.detailMoviesResponse.observeOnce(this@DetailScreenActivity) { response ->
            binding.tvDetailsTitle.text = response.title
            binding.textViewRate.text = response.vote_average.toString()
            binding.textViewDate.text = response.release_date
            binding.textViewOverview.text = response.overview
            Glide.with(this)
                .load(BASE_IMAGE_URL + response.poster_path)
                .optionalCenterCrop()
                .into(binding.detailImageView);
        }
    }

}