package com.example.skillcinema.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.databinding.ItemMovieBinding

class CollectionMovieAdapter(
    private val onClickView: (id: Int) -> Unit
) : RecyclerView.Adapter<CollectionMovieAdapter.CollectionMovieViewHolder>() {

    private var list = listOf<CollectionMovie>()

    fun setData(newList: List<CollectionMovie>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionMovieViewHolder {
        val binding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionMovieViewHolder(binding, onClickView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CollectionMovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class CollectionMovieViewHolder(
        val binding: ItemMovieBinding,
        private val onClickView: (id: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: CollectionMovie) {
            binding.nameTxt.text = movie.name
            binding.genreTxt.text = movie.genre
            binding.shownBtn.isVisible = false
            binding.countTxt.text = movie.score
            binding.countTxt.isVisible = movie.score.isNotEmpty()

            Glide.with(binding.root)
                .load(movie.poster)
                .into(binding.posterImageView)

            binding.root.setOnClickListener {
                onClickView(movie.id)
            }
        }

    }
}