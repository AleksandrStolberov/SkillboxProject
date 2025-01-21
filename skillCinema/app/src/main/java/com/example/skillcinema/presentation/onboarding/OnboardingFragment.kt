package com.example.skillcinema.presentation.onboarding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pictureImageView.setImageResource(requireArguments().getInt(IMAGE_RES))
        binding.infoTextView.setText(requireArguments().getInt(TEXT_RES))

        binding.skipBtn.setOnClickListener {
            onboardingFinished()
            findNavController().navigate(R.id.action_pagerFragment_to_homeFragment)
        }

    }

    private fun onboardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(PREF_KEY, true)
        editor.apply()
    }

    companion object {

        private const val IMAGE_RES = "image"
        private const val TEXT_RES = "text"
        const val PREF_KEY = "Finished"

        fun newInstance(@DrawableRes image: Int, @StringRes text: Int): OnboardingFragment {
            val fragment = OnboardingFragment()
            val bundle =  Bundle().apply {
                putInt(IMAGE_RES, image)
                putInt(TEXT_RES, text)
            }
            fragment.arguments = bundle
            return fragment
        }

    }
    
}