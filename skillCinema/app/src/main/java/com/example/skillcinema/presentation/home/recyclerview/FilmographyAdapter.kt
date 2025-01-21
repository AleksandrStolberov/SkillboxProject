package com.example.skillcinema.presentation.home.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.model.Film
import com.example.skillcinema.databinding.ItemFilmographyBinding

class FilmographyAdapter(
    private val onClickView: (id: Int) -> Unit
) : RecyclerView.Adapter<FilmographyAdapter.FilmographyViewHolder>() {

    private var list: List<Film> = emptyList()

    fun setData(newList: List<Film>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmographyViewHolder {
        val binding =
            ItemFilmographyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmographyViewHolder(binding, onClickView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FilmographyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class FilmographyViewHolder(
        private val binding: ItemFilmographyBinding,
        private val onClickView: (id: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Film) {
            binding.nameTxt.text = item.nameRu ?: item.nameEn
            binding.countTxt.text = item.rating
            binding.filmNameTxt.text = item.description

            binding.root.setOnClickListener {
                onClickView(item.filmId)
            }
        }
    }
}