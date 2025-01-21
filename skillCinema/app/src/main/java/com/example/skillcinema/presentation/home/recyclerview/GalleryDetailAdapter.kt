package com.example.skillcinema.presentation.home.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.model.Image
import com.example.skillcinema.databinding.ItemPictureLargeBinding
import com.example.skillcinema.databinding.ItemPictureSmallBinding

class GalleryDetailAdapter(
    private val onClickView: (index: Int, srcList: List<String>) -> Unit
) : PagingDataAdapter<Image, GalleryDetailAdapter.BaseGalleryViewHolder>(GalleryDiffUtilCallback()) {

    override fun onBindViewHolder(holder: BaseGalleryViewHolder, position: Int) {
        holder.bind(getItem(position) ?: error("Error in GalleryAdapter"), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseGalleryViewHolder {
        return when (viewType) {
            SMALL_ITEM -> {
                val binding =
                    ItemPictureSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GallerySmallHolder(binding, onClickView)
            }

            LARGE_ITEM -> {
                val binding =
                    ItemPictureLargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GalleryLargeHolder(binding, onClickView)
            }

            else -> error("")
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when  {
            (position + 1) % 3 == 0 -> LARGE_ITEM
            else -> SMALL_ITEM
        }
    }

    class GalleryDiffUtilCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.previewUrl == newItem.previewUrl
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }

    }

    abstract class BaseGalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: Image, position: Int)
    }


    inner class GallerySmallHolder(
        private val binding: ItemPictureSmallBinding,
        private val onClickView: (index: Int, srcList: List<String>) -> Unit
    ) : BaseGalleryViewHolder(binding.root) {

        override fun bind(item: Image, position: Int) {
            Glide.with(binding.root)
                .load(item.imageUrl)
                .centerCrop()
                .into(binding.galleryImage)

            val list = this@GalleryDetailAdapter.snapshot().map {
                it?.imageUrl ?: ""
            }

            binding.root.setOnClickListener {
                onClickView(position, list)
            }
        }

    }

    inner class GalleryLargeHolder(
        private val binding: ItemPictureLargeBinding,
        private val onClickView: (index: Int, srcList: List<String>) -> Unit
    ) : BaseGalleryViewHolder(binding.root) {

        override fun bind(item: Image, position: Int) {
            Glide.with(binding.root)
                .load(item.imageUrl)
                .centerCrop()
                .into(binding.galleryImage)

            val list = this@GalleryDetailAdapter.snapshot().map {
                it?.imageUrl ?: ""
            }

            binding.root.setOnClickListener {
                onClickView(position, list)
            }

        }

    }

    companion object {
        private const val LARGE_ITEM = 0
        private const val SMALL_ITEM = 1
    }

}