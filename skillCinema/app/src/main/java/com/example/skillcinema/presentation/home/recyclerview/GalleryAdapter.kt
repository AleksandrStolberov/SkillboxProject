package com.example.skillcinema.presentation.home.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.model.Image
import com.example.skillcinema.databinding.ItemGalleryBinding

class GalleryAdapter(private val onClickView: (position: Int) -> Unit) : RecyclerView.Adapter<GalleryAdapter.GalleryHolder>() {

    private var list = emptyList<Image>()

    fun setData(newList: List<Image>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        val binding =
            ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryHolder(binding, onClickView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
        holder.bind(list[position])
    }

    class GalleryHolder(
        private val binding: ItemGalleryBinding,
        val onClickView: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Image) {
            binding.root.setOnClickListener {
                onClickView(layoutPosition)
            }
            Glide.with(binding.root)
                .load(item.previewUrl)
                .into(binding.galleryItemImageView)
        }

    }
}