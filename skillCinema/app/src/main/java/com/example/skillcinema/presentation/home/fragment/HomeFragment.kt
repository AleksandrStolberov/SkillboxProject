package com.example.skillcinema.presentation.home.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.data.model.State
import com.example.skillcinema.databinding.FragmentHomeBinding
import com.example.skillcinema.presentation.AppActivity
import com.example.skillcinema.presentation.home.recyclerview.HomePageAdapter
import com.example.skillcinema.presentation.home.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var homePageAdapter: HomePageAdapter? = null

    private val viewModel: HomeViewModel by viewModels()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        if (isFirstTime())
            viewModel.getMovies(1)
        mainScope.launch {
            if (!isFirstTime()) {
                stateView(State.Success)
            }
            binding.parentRecyclerView.isVisible = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainScope.launch {
            viewModel.state.collect {
                stateView(it)
            }
        }

        binding.errorBtn.setOnClickListener {
            if (viewModel.movies.value.isEmpty())
                viewModel.getMovies( 1)
            else
                homePageAdapter?.submitList(viewModel.movies.value)
        }

        homePageAdapter = HomePageAdapter(
            { name, collection ->
                val bundle = Bundle()
                bundle.putString(ListPageFragment.NAME_KEY, name)
                bundle.putIntArray(ListPageFragment.COLLECTION_KEY, collection.toIntArray())
                findNavController().navigate(R.id.action_navigation_home_to_listPageFragment, bundle)
            },
            { id ->
                val bundle = Bundle()
                bundle.putInt(FilmPageFragment.ID_KEY, id)
                findNavController().navigate(R.id.action_navigation_home_to_filmPageFragment, bundle)
            }
        )
        if (!isFirstTime()) {
            binding.parentRecyclerView.isVisible = true
            homePageAdapter?.submitList(viewModel.movies.value)
        }

        with(binding.parentRecyclerView) {
            val divider =
                DividerItemDecoration(binding.root.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = homePageAdapter
        }

        mainScope.launch {
            viewModel.movies.collect {
                homePageAdapter?.submitList(it)
            }
        }

    }

    private fun stateView(state: State) {
        when (state) {
            is State.Loading -> {
                visibilityOfView(true)
            }

            is State.Success -> {
                visibilityOfView(false)
            }

            is State.Error -> {
                visibilityOfView(false)
                binding.errorTxt.isVisible = true
                binding.errorBtn.isVisible = true
                binding.errorTxt.text = if (state.message == ERROR_402)
                    resources.getString(R.string.service_unavailable)
                else
                    resources.getString(R.string.unknown_error)
            }
        }

    }

    private fun visibilityOfView(flag: Boolean) {
        binding.pictureImageView.isVisible = flag
        binding.progressIndicator.isVisible = flag
        (requireActivity() as AppActivity).findViewById<BottomNavigationView>(R.id.navView).isVisible =
            !flag
        binding.errorBtn.isVisible = false
        binding.errorTxt.isVisible = false
        binding.errorTxt.text = ""
    }


    private fun isFirstTime(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("isFirstTime", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(AppActivity.KEY_IS_FIRST, false)
    }

    private fun setSharedPref(flag: Boolean) {
        val sharedPref = requireActivity().getSharedPreferences("isFirstTime", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(AppActivity.KEY_IS_FIRST, flag)
        editor.apply()
    }

    override fun toString(): String {
        return "HomeFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        setSharedPref(false)
        homePageAdapter = null
        mainScope.cancel(null)
    }

    companion object {
        private const val ERROR_402 = "HTTP 402 "
    }

}