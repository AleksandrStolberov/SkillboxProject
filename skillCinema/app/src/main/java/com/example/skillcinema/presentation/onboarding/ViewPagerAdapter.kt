package com.example.skillcinema.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.skillcinema.data.model.OnboardingScreens

class ViewPagerAdapter(
    private val list: ArrayList<OnboardingScreens>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val screen = list[position]
        return OnboardingFragment.newInstance(
            screen.imageRes,
            screen.textRes
        )
    }


}