package com.example.skillcinema.presentation.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.skillcinema.MoviesInfo
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentListPageBinding
import com.example.skillcinema.presentation.home.recyclerview.ListPageAdapter
import com.example.skillcinema.presentation.home.viewmodel.ListPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListPageFragment : Fragment() {

    private var _binding: FragmentListPageBinding? = null
    private val binding get() = _binding!!

    private var listPageAdapter: ListPageAdapter? = null

    private val viewModel: ListPageViewModel by viewModels()

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

        val name = arguments?.getString(NAME_KEY) ?: MoviesInfo.TOP_250
        val collection = arguments?.getIntArray(COLLECTION_KEY) ?: intArrayOf(0, 0, 0)
        val pages = collection[2]
        val idMovie = arguments?.getInt(ID_KEY) ?: 0

        if (idMovie != 0) {
            mainScope.launch {
                viewModel.getSimilarMovies(idMovie)
            }
        }

        if (name == MoviesInfo.PREMIERS) {
            mainScope.launch {
                viewModel.getPremiers()
            }
        }


        listPageAdapter = ListPageAdapter { id ->
            val bundle = Bundle()
            bundle.putInt(FilmPageFragment.ID_KEY, id)
            findNavController().navigate(R.id.action_listPageFragment_to_filmPageFragment, bundle)
        }

        with(binding.moreRecyclerView) {
            val divider =
                DividerItemDecoration(binding.root.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                orientation = GridLayoutManager.VERTICAL
            }
            adapter = listPageAdapter
        }

        binding.myToolbar.tittleTxt.text = name


        if (idMovie == 0) {
            if (name == MoviesInfo.PREMIERS) {
                mainScope.launch {
                    viewModel.premiers.collect {
                        listPageAdapter?.submitData(PagingData.from(it))
                    }
                }
            } else {
                mainScope.launch {
                    viewModel.pagedMovies(
                        name,
                        collection.toList(),
                        pages,
                        null
                    ).collect {
                        listPageAdapter?.submitData(it)
                    }
                }
            }
        } else {
            mainScope.launch {
                viewModel.similar.collect {
                    listPageAdapter?.submitData(PagingData.from(it))
                }
            }
        }

        binding.myToolbar.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        listPageAdapter = null
    }

    companion object {
        const val NAME_KEY = "NAME"
        const val COLLECTION_KEY = "COLLECTION_KEY"
        const val ID_KEY = "idMovie"
    }

}