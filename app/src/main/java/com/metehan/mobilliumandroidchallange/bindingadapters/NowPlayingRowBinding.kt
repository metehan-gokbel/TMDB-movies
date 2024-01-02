package com.metehan.mobilliumandroidchallange.bindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.metehan.mobilliumandroidchallange.util.Constants.Companion.BASE_IMAGE_URL

class NowPlayingRowBinding {
    companion object{

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String){
            imageView.load(BASE_IMAGE_URL + imageUrl){
                crossfade(600)
            }
        }

        @BindingAdapter("setTitle")
        @JvmStatic
        fun setTitle(textView: TextView, title: String){
            textView.text = title
        }

        @BindingAdapter("setOverview")
        @JvmStatic
        fun setOverview(textView: TextView, overview: String){
            textView.text = overview
        }

        @BindingAdapter("setVoteAverage")
        @JvmStatic
        fun setVoteAverage(textView: TextView, voteAverage: String){
            textView.text = voteAverage
        }

        @BindingAdapter("setReleaseDate")
        @JvmStatic
        fun setReleaseDate(textView: TextView, releaseDate: String){
            textView.text = releaseDate
        }
    }
}