package com.metehan.mobilliumandroidchallange.data.network

import com.metehan.mobilliumandroidchallange.models.Movies
import com.metehan.mobilliumandroidchallange.models.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MoviesApi {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @QueryMap queries: Map<String, String>
    ): Response<Movies>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @QueryMap queries: Map<String, String>
    ): Response<Movies>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<Result>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @QueryMap queries: Map<String, String>
    ): Response<Movies>
}
