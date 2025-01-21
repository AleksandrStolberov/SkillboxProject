package com.example.skillcinema.presentation.home.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.domain.model.ParentMovie
import com.example.skillcinema.databinding.ItemParentBinding

class HomePageAdapter(
    private val onClick: (name: String, collectionType: List<Int>) -> Unit,
    private val onClickView: (id: Int) -> Unit
) : ListAdapter<ParentMovie, HomePageAdapter.MyHolder>(MovieDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding =
            ItemParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding, onClick, onClickView)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieDiffUtilCallback : DiffUtil.ItemCallback<ParentMovie>() {
        override fun areItemsTheSame(oldItem: ParentMovie, newItem: ParentMovie): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ParentMovie, newItem: ParentMovie): Boolean {
            return oldItem == newItem
        }

    }

    class MyHolder(
        private val binding: ItemParentBinding,
        private val onClick: (name: String, collectionType: List<Int>) -> Unit,
        onClickView: (id: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val movieAdapter = MovieAdapter(onClick, onClickView)

        init {
            binding.info.nextImg.isVisible = false
            val divider =
                DividerItemDecoration(binding.root.context, DividerItemDecoration.HORIZONTAL)
            with(binding.childRecyclerView) {
                setHasFixedSize(true)
                addItemDecoration(divider)
                layoutManager = LinearLayoutManager(binding.root.context).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                    initialPrefetchItemCount = 3
                }
            }
        }

        fun bind(item: ParentMovie) {
            val total = item.total
            val pages = item.totalPages ?: 1
            Log.d("BundleInfo", "collection - ${item.collection}")
            val collection = if (item.collection.isNullOrEmpty())
                listOf(0, 0, pages)
            else
                listOf(item.collection[0], item.collection[1], pages)

            binding.info.allBtn.isVisible = total > 19
            binding.info.captionTxt.text = item.name

            movieAdapter.setData(item.listItem, total)
            movieAdapter.setAdditionalInfo(item.name, collection)
            binding.childRecyclerView.adapter = movieAdapter

            binding.info.allBtn.setOnClickListener {
                onClick(item.name, collection)
            }
        }
    }
}