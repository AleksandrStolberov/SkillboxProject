package com.example.skillcinema.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentCalendarBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
import java.util.Calendar

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settings = App.searchSettings

        binding.toolbar.tittleTxt.text = getString(R.string.period)

        binding.findToTxt.findingTxt.text = getString(R.string.find_to)

        binding.toolbar.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val max = 7
        val min = 0
        var currentFrom = setButtonFrom(settings.yearFrom, settings.currentYear)
        var currentTo = setButtonTo(settings.yearTo, settings.currentYear)

        binding.findFromTxt.backBtn.setOnClickListener {
            if (currentFrom < max) {
                currentFrom++
                setYear(currentFrom, binding.findFromTxt.chipGroup)
            }
        }

        binding.findFromTxt.nextBtn.setOnClickListener {
            if (currentFrom > min) {
                currentFrom--
                setYear(currentFrom, binding.findFromTxt.chipGroup)
            }
        }

        binding.findToTxt.backBtn.setOnClickListener {
            if (currentTo < max) {
                currentTo++
                setYear(currentTo, binding.findToTxt.chipGroup)
            }
        }

        binding.findToTxt.nextBtn.setOnClickListener {
            if (currentTo > min) {
                currentTo--
                setYear(currentTo, binding.findToTxt.chipGroup)
            }
        }

        binding.findFromTxt.chipGroup.forEach { chip ->
            val ch = (chip as Chip)
            ch.setOnClickListener {
                settings.yearFrom(ch.text.toString().toInt())
            }
        }

        binding.findToTxt.chipGroup.forEach { chip ->
            val ch = (chip as Chip)
            ch.setOnClickListener {
                settings.yearTo(ch.text.toString().toInt())
            }
        }

        binding.acceptBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun setYear(current: Int, chipGroup: ChipGroup) {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR) - 11
        var min = currentYear - (11 * current)

       chipGroup.forEach { chip ->
           (chip as Chip).text = min.toString()
           min++
        }

    }

    private fun setButtonFrom(from: Int, currentYear: Int): Int {
        val yearPlace = currentYear - from
        val division = if (yearPlace != 0) yearPlace / 11 else 0
        val chip =
            (binding.findFromTxt.chipGroup.getChildAt(11 - (yearPlace - (11 * division))) as Chip)
        setYear(division, binding.findFromTxt.chipGroup)
        chip.performClick()
        return division
    }

    private fun setButtonTo(to: Int, currentYear: Int): Int {
        val yearPlace = currentYear - to
        val division = if (yearPlace != 0) yearPlace / 12 else 0
        val chip =
            (binding.findToTxt.chipGroup.getChildAt(11 - (yearPlace - (11 * division))) as Chip)
        setYear(division, binding.findToTxt.chipGroup)
        chip.performClick()
        return division
    }

}