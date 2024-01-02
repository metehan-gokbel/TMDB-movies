package com.metehan.mobilliumandroidchallange.models

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Result(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val release_year: String
){
    // Release year null geldiği için release_date'i yyyy-MM-dd formatından  yyyy formatına dönüştürdüm.
    val year: Int
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = dateFormat.parse(release_date)
            val calendar = Calendar.getInstance()
            calendar.time = date ?: Date()

            return calendar.get(Calendar.YEAR)
        }


}