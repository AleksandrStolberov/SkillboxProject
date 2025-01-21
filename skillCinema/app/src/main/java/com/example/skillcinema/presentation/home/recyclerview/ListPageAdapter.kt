package com.example.skillcinema.presentation.home.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import com.example.skillcinema.domain.model.Movie
import com.example.skillcinema.databinding.ItemMovieBinding
import com.example.skillcinema.databinding.ItemSearchMovieBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListPageAdapter(
    private val onClickView: (id: Int) -> Unit
) : PagingDataAdapter<Movie, ListPageAdapter.BaseHolder>(PageDiffUtilCallback()) {

    private var isSearchMovieType = false

    fun setType() {

        isSearchMovieType = true
    }


    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bind(getItem(position) ?: error("Error in ListPageAdapter"))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return when (viewType) {
            MOVIE_TYPE -> {
                val binding =
                    ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ListPageHolder(binding, onClickView)
            }
            SEARCH_MOVIE_TYPE -> {
                val binding =
                    ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ListPageSearchHolder(binding, onClickView)
            } else -> error("Error viewType in ListPageAdapter")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isSearchMovieType)
            SEARCH_MOVIE_TYPE
        else
            MOVIE_TYPE
    }

    class PageDiffUtilCallback : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(movie: Movie)
    }

    class ListPageHolder(
        val binding: ItemMovieBinding,
        private val onClickView: (id: Int) -> Unit
    ) : BaseHolder(binding.root) {

        override fun bind(movie: Movie) {
            binding.nameTxt.text = movie.name
            binding.genreTxt.text = movie.genres
            binding.countTxt.text = movie.score.toString()
            binding.shownBtn.isVisible = false
            binding.countTxt.isVisible = movie.score != null

            binding.root.setOnClickListener { onClickView(movie.id) }

                Glide.with(binding.root)
                    .load(movie.poster)
                    .into(binding.posterImageView)
        }

    }

    class ListPageSearchHolder(
        val binding: ItemSearchMovieBinding,
        private val onClickView: (id: Int) -> Unit
    ) : BaseHolder(binding.root) {

        override fun bind(movie: Movie) {
            binding.nameTxt.text = movie.name
            binding.genreTxt.text = movie.genres
            binding.countTxt.text = movie.score.toString()
            binding.shownBtn.isVisible = false
            binding.countTxt.isVisible = movie.score != null
            binding.yearTxt.text = movie.year

            binding.root.setOnClickListener { onClickView(movie.id) }

            Glide.with(binding.root)
                .load(movie.poster)
                .into(binding.posterImageView)
        }

    }

    companion object {
        private const val MOVIE_TYPE = 0
        private const val SEARCH_MOVIE_TYPE = 1
    }

}