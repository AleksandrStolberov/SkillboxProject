package com.example.skillcinema.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentCountryAndGenreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountryAndGenreFragment : Fragment() {

    private var _binding: FragmentCountryAndGenreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CountryAndGenreViewModel by viewModels()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var countryAndGenreAdapter: CountryAndGenreAdapter? = null

    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryAndGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments?.getString(TYPE_KEY) ?: ""
        val settings = App.searchSettings

        viewModel.getCollectionType()

        binding.toolbar.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        position = if (type == COUNTRY_TYPE) settings.country.keys.first() else settings.genre.keys.first()

        countryAndGenreAdapter = CountryAndGenreAdapter { map ->
            if (type == COUNTRY_TYPE)
                settings.country(map)
            else
                settings.genre(map)
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.toolbar.tittleTxt.text = type
        binding.searchView.paramsIcon.isVisible = false
        binding.searchView.lineIcon.isVisible = false
        binding.searchView.searchEdit.hint =
            "Введите ${if (type == COUNTRY_TYPE) "страну" else "жанр"} "

        with(binding.recyclerView) {
            val divider =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                    ResourcesCompat.getDrawable(resources, R.drawable.divider, null)
                        ?.let { setDrawable(it) }
                }
            addItemDecoration(divider)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = countryAndGenreAdapter
            setHasFixedSize(true)
        }

        var mapForSearch = emptyMap<Int, String>()

        mainScope.launch {
            viewModel.collectionType.collect { collection ->
                if (collection.isNotEmpty()) {
                    val map = if (type == COUNTRY_TYPE)
                        collection[0].countries.associateBy({ it.id }, { it.country })
                    else
                        collection[0].genres.associateBy({ it.id }, { it.genre } )
                    mapForSearch = map
                    countryAndGenreAdapter?.setData(map.filter { it.value.isNotEmpty() }, position)
                }
            }
        }

        binding.searchView.searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filterMap = mapForSearch.filter { m -> m.value.startsWith(s.toString(), true) }
                countryAndGenreAdapter?.setData(filterMap.filter { it.value.isNotEmpty() }, position)
            }

            override fun afterTextChanged(s: Editable?) {}

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        countryAndGenreAdapter = null
    }

    companion object {
        const val TYPE_KEY = "type"
        private const val COUNTRY_TYPE = "Страна"
    }
}