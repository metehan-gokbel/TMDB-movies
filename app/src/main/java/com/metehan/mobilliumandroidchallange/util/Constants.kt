package com.metehan.mobilliumandroidchallange.util

class Constants {
    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original/"
        const val API_KEY = "654927c9d696561a50866b2359b2f026"
        const val QUERY_API_KEY = "api_key"
        const val QUERY_MOVIE_ID = "movie_id"

        // ROOM Database
        const val DATABASE_NAME = "movies_database"
        const val UPCOMING_MOVIES_TABLE = "upcoming_movies_table"

        // Data Store Preferences
        const val PREFERENCES_NAME = "movies_preferences"
        const val PREFERENCES_BACK_ONLINE = "backOnline"
        const val PREFERENCES_MOVIE = "movie"
        const val PREFERENCES_MOVIE_ID = "movieId"
    }
}