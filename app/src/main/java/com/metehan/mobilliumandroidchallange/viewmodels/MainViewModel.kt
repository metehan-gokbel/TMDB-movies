package com.metehan.mobilliumandroidchallange.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metehan.mobilliumandroidchallange.data.Repository
import com.metehan.mobilliumandroidchallange.models.Movies
import com.metehan.mobilliumandroidchallange.models.Result
import com.metehan.mobilliumandroidchallange.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application){


    // REMOTE
    var nowPlayingMoviesResponse: MutableLiveData<NetworkResult<Movies>> = MutableLiveData()
    var popularMoviesResponse: MutableLiveData<NetworkResult<Movies>> = MutableLiveData()
    var detailMoviesResponse: MutableLiveData<Result> = MutableLiveData()
    var upcomingMoviesResponse: MutableLiveData<NetworkResult<Movies>>  = MutableLiveData()

    fun getNowPlayingMovies(queries: Map<String, String>) = viewModelScope.launch {
        getNowPlayingMoviesSafeCall(queries)
    }

    fun getPopularMovies(queries: Map<String, String>) = viewModelScope.launch {
        getPopularMoviesSafeCall(queries)
    }

    fun getMovieDetails(movieId: Int, apiKey: String) = viewModelScope.launch {
        getMovieDetailsSafeCall(movieId, apiKey)
    }

    fun getUpcomingMovies(queries: Map<String, String>) = viewModelScope.launch {
        getUpcomingMoviesSafeCall(queries)
    }

    private suspend fun getNowPlayingMoviesSafeCall(queries: Map<String, String>) {
        nowPlayingMoviesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getNowPlayingMovies(queries)
                nowPlayingMoviesResponse.value = handleNowPlayingMoviesResponse(response)

            } catch (e: Exception) {
                nowPlayingMoviesResponse.value = NetworkResult.Error("Now Playing Movies not found.")
            }
        } else {
            nowPlayingMoviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getUpcomingMoviesSafeCall(queries: Map<String, String>) {
        upcomingMoviesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getUpcomingMovies(queries)

                upcomingMoviesResponse.value = handleNowPlayingMoviesResponse(response)

            } catch (e: Exception) {
                upcomingMoviesResponse.value = NetworkResult.Error("Upcoming Movies not found.")
            }
        } else {
            upcomingMoviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getPopularMoviesSafeCall(queries: Map<String, String>) {
        popularMoviesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getPopularMovies(queries)
//                println("response: " + response.body())
                popularMoviesResponse.value = handleUpcomingMoviesResponse(response)

            } catch (e: Exception) {
                popularMoviesResponse.value = NetworkResult.Error("Popular Movies not found.")
            }
        } else {
            popularMoviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getMovieDetailsSafeCall(movieId: Int, apiKey: String) {
//        detailMoviesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getMovieDetails(movieId, apiKey)
                detailMoviesResponse.value = handleMovieDetailsResponse(response)

            } catch (e: Exception) {
//                detailMoviesResponse.value = NetworkResult.Error("Popular Movies not found.")
            }
        } else {
//            detailMoviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }



    private fun handleNowPlayingMoviesResponse(response: Response<Movies>): NetworkResult<Movies>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }

            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Upcoming Movies not found.")
            }

            response.isSuccessful -> {
                val nowPlayingMovies = response.body()
                return NetworkResult.Success(nowPlayingMovies!!)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleUpcomingMoviesResponse(response: Response<Movies>): NetworkResult<Movies>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }

            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Upcoming Movies not found.")
            }

            response.isSuccessful -> {
                val upcomingMovies = response.body()
                return NetworkResult.Success(upcomingMovies!!)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleMovieDetailsResponse(response: Response<Result>): Result? {
        when {
            response.isSuccessful -> {
                val movieDetails = response.body()
                return movieDetails
            }

            else -> {
//                return NetworkResult.Error(response.message())
                return null
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}