package com.example.skillcinema.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skillcinema.App
import com.example.skillcinema.MoviesInfo
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentSearchBinding
import com.example.skillcinema.presentation.home.fragment.FilmPageFragment
import com.example.skillcinema.presentation.home.recyclerview.ListPageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var movieAdapter: ListPageAdapter? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.searchFragment))
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.navigateUp(navHostFragment, appBarConfiguration)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = ListPageAdapter { id ->
            val bundle = Bundle()
            bundle.putInt(FilmPageFragment.ID_KEY, id)
            findNavController().navigate(R.id.action_searchFragment_to_filmPageFragment2, bundle)

        }

        val settings = App.searchSettings

        binding.searchView.paramsIcon.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_searchSettingsFragment)
        }

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        binding.searchView.searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                settings.keyword(s.toString())

                mainScope.launch {
                    if (!s.isNullOrEmpty() && s.isNotBlank()) {
                        viewModel.pagedMovies(
                            MoviesInfo.SEARCH, listOf(0, 0, 0), 1, settings
                        ).collect { paging ->
                            movieAdapter?.setType()
                            movieAdapter?.submitData(paging)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        movieAdapter = null
    }

}