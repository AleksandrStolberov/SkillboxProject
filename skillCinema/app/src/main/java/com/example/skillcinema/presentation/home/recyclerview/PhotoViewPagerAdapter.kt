package com.example.skillcinema.presentation.home.recyclerview

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.skillcinema.presentation.home.fragment.PhotoFragment

class PhotoViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private var list: List<String> = emptyList()

    fun setData(newList: List<String>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val photo = list[position]
        return PhotoFragment.newInstance(photo)
    }


}