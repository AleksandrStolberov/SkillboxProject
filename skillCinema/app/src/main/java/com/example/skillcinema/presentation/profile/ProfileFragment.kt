package com.example.skillcinema.presentation.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.databinding.DialogCollectionBinding
import com.example.skillcinema.databinding.FragmentProfileBinding
import com.example.skillcinema.presentation.home.fragment.FilmPageFragment
import com.example.skillcinema.presentation.home.recyclerview.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var collectionsAdapter: CollectionsAdapter? = null
    private var shownMovieAdapter: ProfileMovieAdapter? = null
    private var interestedMovieAdapter: ProfileMovieAdapter? = null

    private val viewModel: ProfileViewModel by viewModels()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.profileFragment))
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.navigateUp(navHostFragment, appBarConfiguration)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectionsAdapter = CollectionsAdapter({ id ->
            mainScope.launch {
                viewModel.removeCollectionById(id)
            }
        }, { collectionId, name ->
            val bundle = bundleOf(
                CollectionMovieListFragment.COLLECTION to collectionId.toString(),
                CollectionMovieListFragment.TITLE_TXT to name
            )
            findNavController().navigate(R.id.action_profileFragment_to_collectionMovieListFragment, bundle)
        })

        shownMovieAdapter = ProfileMovieAdapter({
            mainScope.launch {
                viewModel.removeAllShownMovies()
            }
        },{ id ->
            val bundle = bundleOf(FilmPageFragment.ID_KEY to id)
            findNavController().navigate(R.id.action_profileFragment_to_filmPageFragment3, bundle)
        })
        interestedMovieAdapter = ProfileMovieAdapter({
            mainScope.launch {
                viewModel.removeAllCachedMovies()
            }
        },{ id ->
            val bundle = bundleOf(FilmPageFragment.ID_KEY to id)
            findNavController().navigate(R.id.action_profileFragment_to_filmPageFragment3, bundle)
        })
        
        binding.viewedRecyclerView.info.allBtn.setOnClickListener {
            val bundle = bundleOf(
                CollectionMovieListFragment.COLLECTION to CollectionMovieListFragment.SHOW,
                CollectionMovieListFragment.TITLE_TXT to getString(R.string.shown)
                )
            findNavController().navigate(R.id.action_profileFragment_to_collectionMovieListFragment, bundle)
        }
        binding.interestedRecyclerView.info.allBtn.setOnClickListener {
            val bundle = bundleOf(
                CollectionMovieListFragment.COLLECTION to CollectionMovieListFragment.CASHED,
                CollectionMovieListFragment.TITLE_TXT to getString(R.string.interested)
            )
            findNavController().navigate(R.id.action_profileFragment_to_collectionMovieListFragment, bundle)
        }

        binding.viewedRecyclerView.info.captionTxt.text = getString(R.string.shown)
        binding.interestedRecyclerView.info.captionTxt.text = getString(R.string.interested)

        with(binding.viewedRecyclerView.childRecyclerView) {
            setHasFixedSize(true)
            val divider =
                DividerItemDecoration(binding.root.context, DividerItemDecoration.HORIZONTAL)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(binding.root.context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = shownMovieAdapter
        }

        with(binding.interestedRecyclerView.childRecyclerView) {
            setHasFixedSize(true)
            val divider =
                DividerItemDecoration(binding.root.context, DividerItemDecoration.HORIZONTAL)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(binding.root.context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = interestedMovieAdapter
        }

        with(binding.collectionRecyclerView) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(binding.root.context, 2).apply {
                orientation = GridLayoutManager.HORIZONTAL
            }
            adapter = collectionsAdapter
        }


        mainScope.launch {
            viewModel.getAllCollections().collect {
                collectionsAdapter?.setData(it)
            }
        }

        mainScope.launch {
            viewModel.getAllShownMovies().collect { movies ->
                if (movies.isNotEmpty()) {
                    binding.viewedRecyclerView.info.nextImg.isVisible = true
                    movies.size.toString().also { binding.viewedRecyclerView.info.allTxt.text = it }
                } else {
                    binding.viewedRecyclerView.info.nextImg.isVisible = false
                    binding.viewedRecyclerView.info.allTxt.text = ""
                }
                shownMovieAdapter?.setData(movies)
            }
        }

        mainScope.launch {
            viewModel.getAllCachedMovies().collect { movies ->
                if (movies.isNotEmpty()) {
                    binding.interestedRecyclerView.info.nextImg.isVisible = true
                    movies.size.toString()
                        .also { binding.interestedRecyclerView.info.allTxt.text = it }
                } else {
                    binding.interestedRecyclerView.info.nextImg.isVisible = false
                    binding.interestedRecyclerView.info.allTxt.text = ""
                }
                interestedMovieAdapter?.setData(movies)
            }
        }

        binding.createBtn.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog() {
        val bindingDialog = DialogCollectionBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false)
        bindingDialog.acceptBtn.setOnClickListener {
            val name = bindingDialog.searchEdit.text.toString()
            if (name.isNotEmpty() && name.isNotBlank()) {
                mainScope.launch {
                    viewModel.addNewCollection(name)
                }
            }
            dialog?.dismiss()
        }
        bindingDialog.closeBtn.setOnClickListener {
            dialog?.dismiss()
        }
        dialog = AlertDialog.Builder(requireContext())
            .setView(bindingDialog.root)
            .show()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        collectionsAdapter = null
        shownMovieAdapter = null
        interestedMovieAdapter = null
    }
}