package com.example.skillcinema.presentation.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ItemDeleteBinding
import com.example.skillcinema.databinding.ItemMovieBinding
import com.example.skillcinema.domain.model.Movie
import com.example.skillcinema.presentation.home.recyclerview.MovieAdapter.BaseHolder

class ProfileMovieAdapter(
    private val onClick: () -> Unit,
    private val onClickView: (id: Int) -> Unit
) : RecyclerView.Adapter<ProfileMovieAdapter.BaseHolder>()  {

    private var list = emptyList<Movie>()

    fun setData(newList: List<Movie>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return when (viewType) {
            MOVIE_TYPE -> {
                val binding =
                    ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieHolder(binding, onClickView)
            }

            CLEAR_HISTORY -> {
                val binding =
                    ItemDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ClearHistoryViewHolder(binding, onClick)
            }

            else -> throw IllegalArgumentException("Illrgal type: $viewType")
        }
    }

    override fun getItemCount(): Int {
        return if (list.isNotEmpty())
            list.size + 1
        else
            0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size)
            CLEAR_HISTORY
        else
            MOVIE_TYPE
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (list.isNotEmpty() && position == list.size)
            holder.bind(list[0])
        else
            holder.bind(list[position])
    }

    abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(movie: Movie)
    }

    class MovieHolder(
        private val binding: ItemMovieBinding,
        private val onClickView: (id: Int) -> Unit
    ) : BaseHolder(binding.root) {

        override fun bind(movie: Movie) {
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

    class ClearHistoryViewHolder(
        private val binding: ItemDeleteBinding,
        private val onClick: () -> Unit
    ) : BaseHolder(binding.root) {

        override fun bind(movie: Movie) {
            binding.root.setOnClickListener {
                onClick()
            }
        }

    }

    companion object {
        private const val MOVIE_TYPE = 1
        private const val CLEAR_HISTORY = 2
    }

}