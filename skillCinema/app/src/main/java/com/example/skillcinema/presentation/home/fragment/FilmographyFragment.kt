package com.example.skillcinema.presentation.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentFilmographyBinding
import com.example.skillcinema.presentation.home.recyclerview.FilmographyAdapter
import com.example.skillcinema.presentation.home.viewmodel.FilmographyViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmographyFragment : Fragment() {

    private var _binding: FragmentFilmographyBinding? = null
    private val binding get() = _binding!!
    private var filmographyAdapter: FilmographyAdapter? = null

    private val viewModel: FilmographyViewModel by viewModels()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmographyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(ID_KEY) ?: 0
        val sex = arguments?.getString(SEX_KEY)

        binding.actorNameTxt.text = arguments?.getString(NAME_KEY)

        viewModel.getFilmography(id)

        filmographyAdapter = FilmographyAdapter {
            val bundle = bundleOf(FilmPageFragment.ID_KEY to it)
            if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
                findNavController().navigate(R.id.action_filmographyFragment_to_filmPageFragment, bundle)
            else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
                findNavController().navigate(R.id.action_filmographyFragment2_to_filmPageFragment2, bundle)
            else
                findNavController().navigate(R.id.action_filmographyFragment3_to_filmPageFragment3, bundle)
        }

        with(binding.filmographyRecyclerView) {
            val divider =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                    ResourcesCompat.getDrawable(resources, R.drawable.divider, null)?.let { setDrawable(it) }
                }
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = filmographyAdapter
            setHasFixedSize(true)
        }

        binding.myToolbar.tittleTxt.text = getString(R.string.filmography)

        binding.myToolbar.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        mainScope.launch {
            viewModel.filmography.collect { film ->
                val actor = film.filter { it.professionKey == "ACTOR" }
                val producer = film.filter { it.professionKey == "PRODUCER" }
                val self = film.filter { it.professionKey == "HIMSELF" || it.professionKey == "HERSELF" }

                chipName(sex ?: "", actor.size, producer.size, self.size)

                binding.chipGroup.getChildAt(0).setOnClickListener {
                    binding.chipGroup.getChildAt(0)
                    filmographyAdapter?.setData(actor)
                }
                binding.chipGroup.getChildAt(0).performClick()
                binding.chipGroup.getChildAt(1).setOnClickListener {
                    filmographyAdapter?.setData(producer)
                }
                binding.chipGroup.getChildAt(2).setOnClickListener {
                    filmographyAdapter?.setData(self)
                }

            }
        }
    }

    private fun chipName(sex: String, actor: Int, prod: Int, self: Int) {
        (binding.chipGroup.getChildAt(0) as Chip).text =
            if (sex == "MALE") "Актер $actor" else "Актриса $actor"
        (binding.chipGroup.getChildAt(1) as Chip).text = "Продюсер $prod"
        (binding.chipGroup.getChildAt(2) as Chip).text =
            if (sex == "MALE") "Актер: играет сам себя $self" else "Актриса: играет саму себя $self"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        filmographyAdapter = null
    }

    companion object {
        const val ID_KEY = "staffId"
        const val SEX_KEY = "sex"
        const val NAME_KEY = "name"
    }
}