package com.example.skillcinema.presentation.home.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ItemActorBinding
import com.example.skillcinema.domain.model.Staff

class StaffAdapter(
    private val onClickView: (id: Int) -> Unit,
    private val onClick: () -> Unit
) : RecyclerView.Adapter<StaffAdapter.StaffHolder>() {

    private var list = emptyList<Staff>()

    fun setData(newList: List<Staff>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffHolder {
       val binding =
           ItemActorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffHolder(binding, onClickView, onClick)
    }

    override fun getItemCount(): Int {
        return if (list.size > 19)
            19
        else
            list.size
    }


    override fun onBindViewHolder(holder: StaffHolder, position: Int) {
        holder.bind(list[position])
    }

    class StaffHolder(
        private val binding: ItemActorBinding,
        private val onClickView: (id: Int) -> Unit,
        private val onClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Staff) {
            binding.actorNameTxt.text = item.name
            binding.professionTxt.text = getProfession(item)
            Glide.with(binding.root)
                .load(item.poster)
                .into(binding.avatarImageView)

            binding.root.setOnClickListener {
                onClickView(item.id)
            }

        }

        private fun getProfession(item: Staff): String {
            return if (item.isActor)
                item.movieName
            else
                item.profession.removeRange(item.profession.lastIndex, item.profession.lastIndex + 1)
        }

    }
}