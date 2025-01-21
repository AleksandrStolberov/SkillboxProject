package com.example.skillcinema.presentation.home.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ItemStaffListBinding
import com.example.skillcinema.domain.model.Staff

class StaffListAdapter(
    private val onClickView: (id: Int) -> Unit
) : RecyclerView.Adapter<StaffListAdapter.StaffListViewHolder>() {

    private var list = emptyList<Staff>()

    fun setData(newList: List<Staff>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffListViewHolder {
        val binding =
            ItemStaffListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffListViewHolder(binding, onClickView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StaffListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class StaffListViewHolder(
        val binding: ItemStaffListBinding,
        private val onClickView: (id: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Staff) {
            if (item.name.isEmpty())
                binding.nameRuTxt.text = item.nameEn
            else
                binding.nameRuTxt.text = item.name
            binding.nameEnTxt.text = item.nameEn
            if (item.nameEn.isEmpty())
                binding.nameEnTxt.text = getProfession(item)
            else
                binding.movieNameTxt.text = getProfession(item)

            Glide.with(binding.root)
                .load(item.poster)
                .into(binding.photoImageView)

            binding.root.setOnClickListener {
                onClickView(item.id)
            }
        }

        private fun getProfession(item: Staff): String {
            return if (item.isActor)
                item.movieName
            else
                item.profession.removeRange(
                    item.profession.lastIndex,
                    item.profession.lastIndex + 1
                )

        }

    }


}