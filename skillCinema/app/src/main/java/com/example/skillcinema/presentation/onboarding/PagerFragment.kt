package com.example.skillcinema.presentation.onboarding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentPagerBinding

class PagerFragment : Fragment() {

    private var _binding: FragmentPagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ViewPagerAdapter
    private val viewModel: PagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isOnboardingFinished()) {
            findNavController().navigate(R.id.action_pagerFragment_to_homeFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagerBinding.inflate(inflater, container, false)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.pagerFragment))
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.navigateUp(navHostFragment, appBarConfiguration)
        adapter = ViewPagerAdapter(viewModel.getScreen(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)
    }

    private fun isOnboardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(OnboardingFragment.PREF_KEY, false)
    }

}