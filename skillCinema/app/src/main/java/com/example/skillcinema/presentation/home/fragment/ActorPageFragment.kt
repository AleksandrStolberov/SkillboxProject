package com.example.skillcinema.presentation.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentActorPageBinding
import com.example.skillcinema.domain.model.StaffPage
import com.example.skillcinema.presentation.home.recyclerview.MovieAdapter
import com.example.skillcinema.presentation.home.viewmodel.StaffPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActorPageFragment : Fragment() {

    private var _binding: FragmentActorPageBinding? = null
    private val binding get() = _binding!!
    private var movieAdapter: MovieAdapter? = null

    private val viewModel: StaffPageViewModel by viewModels()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorPageBinding.inflate(inflater, container, false)
        binding.myToolbar.tittleTxt.isVisible = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val staffId = arguments?.getInt(ID_KEY) ?: 0

        viewModel.getStaffPage(staffId)

        movieAdapter = MovieAdapter(
            { _, _ -> }, { id ->
                val bundle = bundleOf(FilmPageFragment.ID_KEY to id)
                if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
                    findNavController().navigate(R.id.action_actorPageFragment_to_filmPageFragment, bundle)
                else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
                    findNavController().navigate(R.id.action_actorPageFragment2_to_filmPageFragment2, bundle)
                else
                    findNavController().navigate(R.id.action_actorPageFragment3_to_filmPageFragment3, bundle)
            })

        with(binding.bestMovieRecyclerView) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val divider =
                DividerItemDecoration(binding.root.context, DividerItemDecoration.HORIZONTAL)
            addItemDecoration(divider)
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        var sex = ""
        var name = ""

        mainScope.launch {
            viewModel.staffPage.collect {
                if (it.isNotEmpty()) {
                    val page = it[0]
                    sex = page.sex
                    name = page.name
                    movieAdapter?.setData(page.movieList, 1)
                    setInfo(page)
                    binding.waitingImg.isVisible = false
                }
            }
        }

        binding.toListBtn.setOnClickListener {
            val bundle = bundleOf(
                FilmographyFragment.ID_KEY to staffId,
                FilmographyFragment.SEX_KEY to sex,
                FilmographyFragment.NAME_KEY to name
            )
            if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
                findNavController().navigate(R.id.action_actorPageFragment_to_filmographyFragment, bundle)
            else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
                findNavController().navigate(R.id.action_actorPageFragment2_to_filmographyFragment2, bundle)
            else
                findNavController().navigate(R.id.action_actorPageFragment3_to_filmographyFragment3, bundle)
        }

        binding.myToolbar.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setInfo(page: StaffPage) {
        binding.actorNameTxt.text = page.name
        page.movieCount.toString().also { binding.countMoviesTxt.text = it }
        binding.actorAgeTxt.text = page.age
        binding.professionTxt.text = page.profession

        Glide.with(binding.root)
            .load(page.posterUrl)
            .into(binding.photoImageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        movieAdapter = null
    }

    companion object {
        const val ID_KEY = "staffId"
    }
}