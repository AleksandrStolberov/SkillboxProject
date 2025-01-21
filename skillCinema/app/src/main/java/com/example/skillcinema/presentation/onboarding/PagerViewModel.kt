package com.example.skillcinema.presentation.onboarding

import androidx.lifecycle.ViewModel
import com.example.skillcinema.R
import com.example.skillcinema.data.model.OnboardingScreens

class PagerViewModel : ViewModel() {

    private val screens = arrayListOf(
        OnboardingScreens(R.drawable.onboarding_1, R.string.onboarding_1),
        OnboardingScreens(R.drawable.onboarding_2, R.string.onboarding_2),
        OnboardingScreens(R.drawable.onboarding_3, R.string.onboarding_3)
    )

    fun getScreen(): ArrayList<OnboardingScreens> {
        return screens
    }

}