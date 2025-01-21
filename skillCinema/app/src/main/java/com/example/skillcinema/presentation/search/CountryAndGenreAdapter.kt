package com.example.skillcinema.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ItemCountryAndGenreBinding

class CountryAndGenreAdapter(
    private val onClickView: (map: Map<Int, String>) -> Unit
) : RecyclerView.Adapter<CountryAndGenreAdapter.MyHolder>(){

    private var pos = 1
    private var map = emptyMap<Int, String>()

    fun setData(newMap: Map<Int, String>, newPos: Int) {
        map = newMap
        pos = newPos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding =
            ItemCountryAndGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding, onClickView)
    }

    override fun getItemCount(): Int {
        return map.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val list = map.values.toList()
        holder.bind(list[position], map.entries.find { it.value == list[position] }!!.key, pos)
    }

    class MyHolder(
        private val binding: ItemCountryAndGenreBinding,
        val onClickView: (map: Map<Int, String>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, key: Int, position: Int) {
            val str = item.replaceFirstChar { ch -> ch.uppercaseChar() }
            binding.typeTxt.text = str

            if (key == position)
                binding.root.setBackgroundColor(binding.root.resources.getColor(R.color.grey_100, null))
            else
                binding.root.setBackgroundColor(binding.root.resources.getColor(R.color.white, null))

            binding.root.setOnClickListener {
                onClickView(mapOf(key to str))
            }
        }

    }

}