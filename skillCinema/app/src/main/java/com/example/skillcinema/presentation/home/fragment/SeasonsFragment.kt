package com.example.skillcinema.presentation.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentSeasonsBinding
import com.example.skillcinema.domain.model.Episode
import com.example.skillcinema.presentation.home.recyclerview.SeasonsAdapter
import com.example.skillcinema.presentation.home.viewmodel.SeasonsViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeasonsFragment : Fragment() {

    private var _binding: FragmentSeasonsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SeasonsViewModel by viewModels()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var seasonsAdapter: SeasonsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(ID_KEY) ?: 0
        binding.myToolbar.tittleTxt.text = arguments?.getString(NAME_KEY) ?: ""
        viewModel.getSeasons(id)

        seasonsAdapter = SeasonsAdapter()

        binding.myToolbar.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        with(binding.seasonsRecyclerView) {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = seasonsAdapter
        }

        mainScope.launch {
            viewModel.seasons.collect { seasons ->
                if (seasons.isNotEmpty()) {
                    binding.chipGroup.getChildAt(0).setOnClickListener {
                        "1 сезон, ${seasons[0].series.size} серий".also { binding.seasonsSeriesTxt.text = it }
                        seasonsAdapter?.setData(seasons[0].series)
                    }
                    binding.chipGroup.getChildAt(0).performClick()
                    for (i in 1..<seasons.size) {
                        setChips((i + 1).toString(), seasons[i].series)
                    }
                }
            }
        }


    }

    private fun setChips(name: String, series: List<Episode>) {
        val chip =  layoutInflater.inflate(R.layout.single_chip_layout, binding.chipGroup, false) as Chip
        chip.text = name
        chip.setOnClickListener {
            "$name сезон, ${series.size} серий".also { binding.seasonsSeriesTxt.text = it }
            seasonsAdapter?.setData(series)
        }
        binding.chipGroup.addView(chip)
    }

    override fun onDestroy() {
        super.onDestroy()
        seasonsAdapter = null
        _binding = null
    }

    companion object {
        const val ID_KEY = "id_key"
        const val NAME_KEY = "name_key"
    }
}