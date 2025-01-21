package com.example.skillcinema.presentation.home.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import com.example.skillcinema.databinding.ItemMyCollectionBinding

class MyCollectionAdapter(
    private val onClick: (flag: Boolean, id: Int) -> Unit
) : RecyclerView.Adapter<MyCollectionAdapter.MyCollectionViewHolder>() {

    private var collections = listOf<Int>()
    private var list = listOf<MyCollection>()

    fun setCollectionsId(newList: List<Int>) {
        collections = newList
    }

    fun setData(newList: List<MyCollection>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCollectionViewHolder {
        val binding =
            ItemMyCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyCollectionViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyCollectionViewHolder, position: Int) {
        holder.bind(list[position], collections, position)
    }

    class CollectionsDiffUtilCallback : DiffUtil.ItemCallback<MyCollection>() {
        override fun areItemsTheSame(oldItem: MyCollection, newItem: MyCollection): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MyCollection, newItem: MyCollection): Boolean {
            return oldItem == newItem
        }

    }

    class MyCollectionViewHolder(
        val binding: ItemMyCollectionBinding,
        val onClick: (flag: Boolean, id: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyCollection, collections: List<Int>, position: Int) {

            binding.checkBox.isChecked = collections.contains(item.id)

            if (binding.checkBox.isChecked) {
                binding.checkBox.setOnClickListener {
                    onClick(true, item.id)
                }
            } else {
                binding.checkBox.setOnClickListener {
                    onClick(false, item.id)
                }
            }

            binding.collectionNameTxt.text = item.name
            binding.countMovieTxt.text = item.countMovie.toString()
        }

    }
}