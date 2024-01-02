package com.metehan.mobilliumandroidchallange.data

import com.metehan.mobilliumandroidchallange.data.network.MoviesApi
import com.metehan.mobilliumandroidchallange.models.Movies
import com.metehan.mobilliumandroidchallange.models.Result
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val moviesApi: MoviesApi
) {
    suspend fun getNowPlayingMovies(queries: Map<String, String>): Response<Movies> {
        return moviesApi.getNowPlayingMovies(queries)
    }

    suspend fun getUpcomingMovies(queries: Map<String, String>): Response<Movies> {
        return moviesApi.getUpcomingMovies(queries)
    }

    suspend fun getPopularMovies(queries: Map<String, String>): Response<Movies> {
        return moviesApi.getPopularMovies(queries)
    }

    suspend fun getMovieDetails(movieId: Int, apiKey: String): Response<Result> {
        return moviesApi.getMovieDetails(movieId, apiKey)
    }
}