package com.example.skillcinema.presentation.home.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.databinding.ItemSeriesBinding
import com.example.skillcinema.domain.model.Episode

class SeasonsAdapter : RecyclerView.Adapter<SeasonsAdapter.SeasonsViewHolder>() {

    private var list: List<Episode> = emptyList()

    fun setData(newList: List<Episode>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonsViewHolder {
        val binding =
            ItemSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeasonsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SeasonsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class SeasonsViewHolder(
        private val binding: ItemSeriesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Episode) {
            binding.nameSeriesTxt.text = item.name
            binding.seasonTxt.text = item.number
            binding.dateTxt.text = item.date

        }
    }
}