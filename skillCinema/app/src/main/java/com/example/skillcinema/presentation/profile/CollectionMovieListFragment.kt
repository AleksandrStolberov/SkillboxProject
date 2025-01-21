package com.example.skillcinema.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.databinding.FragmentListPageBinding
import com.example.skillcinema.presentation.home.fragment.FilmPageFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionMovieListFragment : Fragment() {

    private var _binding: FragmentListPageBinding? = null
    private val binding get() = _binding!!

    private var collectionMovieAdapter: CollectionMovieAdapter? = null

    private val viewModel: CollectionMovieViewModel by viewModels()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myToolbar.tittleTxt.text = arguments?.getString(TITLE_TXT) ?: ""

        binding.myToolbar.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        collectionMovieAdapter = CollectionMovieAdapter { id ->
            val bundle = bundleOf(FilmPageFragment.ID_KEY to id)
            findNavController().navigate(R.id.action_collectionMovieListFragment_to_filmPageFragment3, bundle)
        }

        val collection = arguments?.getString(COLLECTION) ?: "1"

        if (collection == SHOW) {
            mainScope.launch {
                viewModel.getAllShownMovies().collect { movies ->
                    collectionMovieAdapter?.setData(movies.map {
                        CollectionMovie(it.id, it.name, it.genres, it.poster, it.score.toString(), it.year)
                    })
                }
            }
        }
        else if (collection == CASHED) {
            mainScope.launch {
                viewModel.getAllCachedMovies().collect { movies ->
                    collectionMovieAdapter?.setData(movies.map {
                        CollectionMovie(it.id, it.name, it.genres, it.poster, it.score.toString(), it.year)
                    })
                }
            }
        }
        else
            viewModel.getMovieByCollectionId(collection.toInt())


        with(binding.moreRecyclerView) {
            val divider =
                DividerItemDecoration(binding.root.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                orientation = GridLayoutManager.VERTICAL
            }
            adapter = collectionMovieAdapter
        }

        mainScope.launch {
            viewModel.collectionMovies.collect {
                if (it.isNotEmpty()) {
                    collectionMovieAdapter?.setData(it[0].movies)
                }
            }
        }

    }

    companion object {
        const val COLLECTION = "collection"
        const val SHOW = "shown"
        const val CASHED = "cashed"
        const val TITLE_TXT = "TITLE_TXT"
    }
}