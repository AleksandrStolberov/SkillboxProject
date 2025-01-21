package com.example.skillcinema.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.R
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import com.example.skillcinema.data.model.Collection
import com.example.skillcinema.databinding.ItemCollectionBinding

class CollectionsAdapter(
    private val onClickDelete: (id: Int) -> Unit,
    private val onItemClick: (collectionId: Int, name: String) -> Unit
) : RecyclerView.Adapter<CollectionsAdapter.CollectionsHolder>() {

    private var list = listOf<MyCollection>()

    fun setData(newList: List<MyCollection>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsHolder {
        val binding =
            ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionsHolder(binding, onClickDelete, onItemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CollectionsHolder, position: Int) {
        holder.bind(list[position])
    }

    class CollectionsHolder(
        private val binding: ItemCollectionBinding,
        val onClickDelete: (id: Int) -> Unit,
        private val onItemClick: (collectionId: Int, name: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyCollection) {
            if (item.name != "Любимые" && item.name != "Хочу посмотреть")
                binding.collectionImg.setImageResource(R.drawable.profile_image)
            binding.collectionTxt.text = item.name
            binding.countTxt.text = item.countMovie.toString()
            binding.deleteBtn.isVisible = item.id > 2

            binding.deleteBtn.setOnClickListener {
                onClickDelete(item.id)
            }

            binding.root.setOnClickListener {
                onItemClick(item.id, item.name)
            }
        }

    }
}