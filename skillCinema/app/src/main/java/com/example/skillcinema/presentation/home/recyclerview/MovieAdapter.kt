package com.example.skillcinema.presentation.home.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.skillcinema.domain.model.Movie
import com.example.skillcinema.databinding.ItemMovieBinding
import com.example.skillcinema.databinding.ItemShowMoreBinding
import java.lang.StringBuilder

class MovieAdapter(
    private val onClick: (name: String, collectionType: List<Int>) -> Unit,
    private val onClickView: (id: Int) -> Unit
) : RecyclerView.Adapter<MovieAdapter.BaseHolder>() {

    private var movies = emptyList<Movie>()
    private var total = 0
    private var name = ""
    private var collection: List<Int> = emptyList()

    fun setData(list: List<Movie>, newTotal: Int) {
        movies = list
        total = newTotal
        notifyDataSetChanged()
    }

    fun setAdditionalInfo(newName: String, collectionType: List<Int>) {
        collection = collectionType
        name = newName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return when (viewType) {
            MOVIE_TYPE -> {
                val binding =
                    ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieHolder(binding, onClickView)
            }

            SHOW_MORE -> {
                val binding =
                    ItemShowMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ShowMoreViewHolder(binding, onClick)
            }

            else -> throw IllegalArgumentException("Illrgal type: $viewType")
        }
    }

    override fun getItemCount(): Int {
        return if (total > 19)
            19
        else
            movies.size

    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (movies.isNotEmpty())
            holder.bind(movies[position], name, collection)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 18 && movies.size >= 18)
            SHOW_MORE
        else
            MOVIE_TYPE
    }

    abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(movie: Movie, name: String, collectionType: List<Int>)
    }

    class MovieHolder(
        private val binding: ItemMovieBinding,
        private val onClickView: (id: Int) -> Unit
    ) : BaseHolder(binding.root) {

        override fun bind(movie: Movie, name: String, collectionType: List<Int>) {
            binding.shownBtn.isVisible = movie.isShown
            binding.nameTxt.text = movie.name
            binding.genreTxt.text = movie.genres
            binding.countTxt.text = movie.score.toString()
            binding.countTxt.isVisible = movie.score != null
            val id = movie.id

            binding.root.setOnClickListener {
                onClickView(id)
            }

            Glide.with(binding.root)
                .load(movie.poster)
                .into(binding.posterImageView)
        }
    }

    class ShowMoreViewHolder(
        private val binding: ItemShowMoreBinding,
        private val onClick: (name: String, collectionType: List<Int>) -> Unit
    ) : BaseHolder(binding.root) {

        override fun bind(movie: Movie, name: String, collectionType: List<Int>) {
            binding.root.setOnClickListener {
                onClick(name, collectionType)
            }
        }

    }

    companion object {
        private const val MOVIE_TYPE = 1
        private const val SHOW_MORE = 2
    }
}