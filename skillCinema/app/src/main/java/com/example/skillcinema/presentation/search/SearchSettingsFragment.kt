package com.example.skillcinema.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.App
import com.example.skillcinema.MoviesInfo
import com.example.skillcinema.R
import com.example.skillcinema.data.model.SearchSettings
import com.example.skillcinema.databinding.FragmentSearchSettingsBinding
import com.google.android.material.slider.RangeSlider

class SearchSettingsFragment : Fragment() {

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settings = App.searchSettings
        settings.currentYear

        binding.toolbar.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        setInfo()
        radioType(settings.type)
        radioListener(settings)
        radioOrder(settings.order)
        radioOrderListener(settings)
        shown(settings)

        binding.set1.optionTxt.text = settings.country.values.joinToString(" ")

        binding.set2.optionTxt.text = settings.genre.values.joinToString(" ")
        binding.set3.optionTxt.text = "с ${settings.yearFrom} до ${settings.yearTo}"

        binding.set1.root.setOnClickListener {
            findNavController()
                .navigate(R.id.action_searchSettingsFragment_to_countryAndGenreFragment,
                    bundleOf(CountryAndGenreFragment.TYPE_KEY to binding.set1.typeTxt.text.toString()))
        }

        binding.set2.root.setOnClickListener {
            findNavController()
                .navigate(R.id.action_searchSettingsFragment_to_countryAndGenreFragment,
                    bundleOf(CountryAndGenreFragment.TYPE_KEY to binding.set2.typeTxt.text.toString()))
        }

        binding.set3.root.setOnClickListener {
            findNavController().navigate(R.id.action_searchSettingsFragment_to_calendarFragment)
        }



        var rFrom = settings.ratingFrom.toInt()
        var rTo = settings.ratingTo.toInt()



        binding.set4.slider.setValues(rFrom.toFloat(), rTo.toFloat())
        binding.set4.optionTxt.text = if (rFrom == 0 && rTo == 10) "Любой" else "с $rFrom до $rTo"

        binding.set4.slider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {}

            override fun onStopTrackingTouch(slider: RangeSlider) {
                rFrom = slider.values[0].toInt()
                rTo = slider.values[1].toInt()
                binding.set4.optionTxt.text = if (rFrom == 0 && rTo == 10) "Любой" else "с $rFrom до $rTo"
                settings
                    .ratingFrom(rFrom.toDouble())
                    .ratingTo(rTo.toDouble())
            }
        })

        binding.shownBtn.setOnClickListener {
            settings.isShown(!settings.isShown)
            shown(settings)
        }

    }

    private fun shown(settings: SearchSettings) {
        binding.shownImg.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                if (settings.isShown) R.drawable.shown_blue else R.drawable.no_shown,
                null
            )
        )
        binding.shownTxt.text = if (settings.isShown) "Просмотрен" else "Не просмотрен"
    }

    private fun radioListener(settings: SearchSettings) {
        binding.radioGroupType.radioGroup.getChildAt(0).setOnClickListener {
            settings.type(MoviesInfo.ALL)
        }
        binding.radioGroupType.radioGroup.getChildAt(1).setOnClickListener {
            settings.type(MoviesInfo.FILM)
        }
        binding.radioGroupType.radioGroup.getChildAt(2).setOnClickListener {
            settings.type(MoviesInfo.SERIES)
        }
    }

    private fun radioOrderListener(settings: SearchSettings) {
        binding.radioGroupSort.radioGroup.getChildAt(0).setOnClickListener {
            settings.order(MoviesInfo.ORDER_BY_YEAR)
        }
        binding.radioGroupSort.radioGroup.getChildAt(1).setOnClickListener {
            settings.order(MoviesInfo.ORDER_BY_NUM_VOTE)
        }
        binding.radioGroupSort.radioGroup.getChildAt(2).setOnClickListener {
            settings.order(MoviesInfo.ORDER_BY_RATING)
        }
    }

    private fun radioType(type: String) {
        when (type) {
            MoviesInfo.ALL -> binding.radioGroupType.radioGroup.getChildAt(0).performClick()
            MoviesInfo.FILM -> binding.radioGroupType.radioGroup.getChildAt(1).performClick()
            MoviesInfo.SERIES -> binding.radioGroupType.radioGroup.getChildAt(2).performClick()

        }
    }

    private fun radioOrder(type: String) {
        when (type) {
            MoviesInfo.ORDER_BY_YEAR -> binding.radioGroupSort.radioGroup.getChildAt(0).performClick()
            MoviesInfo.ORDER_BY_NUM_VOTE -> binding.radioGroupSort.radioGroup.getChildAt(1).performClick()
            MoviesInfo.ORDER_BY_RATING -> binding.radioGroupSort.radioGroup.getChildAt(2).performClick()

        }
    }

    private fun setInfo() {
        binding.radioGroupSort.showTxt.text = "Сортирвать"
        (binding.radioGroupSort.radioGroup.getChildAt(0) as RadioButton).text = "Дата"
        (binding.radioGroupSort.radioGroup.getChildAt(1) as RadioButton).text = "Популярность"
        (binding.radioGroupSort.radioGroup.getChildAt(2) as RadioButton).text = "Рейтинг"

        binding.toolbar.tittleTxt.text = "Настройки поиска"
        binding.set1.typeTxt.text = "Страна"
        binding.set2.typeTxt.text = "Жанр"
        binding.set3.typeTxt.text = "Год"

    }

}