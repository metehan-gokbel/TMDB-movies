package com.metehan.mobilliumandroidchallange.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.metehan.mobilliumandroidchallange.databinding.NpMoviesRowLayoutBinding
import com.metehan.mobilliumandroidchallange.models.Movies
import com.metehan.mobilliumandroidchallange.util.MoviesDiffUtil
import com.metehan.mobilliumandroidchallange.models.Result

class NpMoviesAdapter : RecyclerView.Adapter<NpMoviesAdapter.MyViewHolder>(){

    private var nowPlayingMovies = emptyList<Result>()
    private var onItemClick: ((Result) -> Unit)? = null


    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClick = listener
    }

    class MyViewHolder(private val binding: NpMoviesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.result = result
            // Update layout whenever there is a change inside our data
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NpMoviesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return nowPlayingMovies.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMovie = nowPlayingMovies[position]
        holder.bind(currentMovie)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentMovie)
        }
    }

    fun setData(newData: Movies) {
        val recipesDiffUtil = MoviesDiffUtil(nowPlayingMovies, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        nowPlayingMovies = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}