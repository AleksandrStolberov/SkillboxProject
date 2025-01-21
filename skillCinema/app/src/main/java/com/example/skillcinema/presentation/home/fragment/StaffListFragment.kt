package com.example.skillcinema.presentation.home.fragment

import android.os.Bundle
import android.util.Log
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
import com.example.skillcinema.databinding.FragmentStaffListBinding
import com.example.skillcinema.presentation.home.recyclerview.StaffListAdapter
import com.example.skillcinema.presentation.home.viewmodel.StaffListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StaffListFragment : Fragment() {

    private var _binding: FragmentStaffListBinding? = null
    private val binding get() = _binding!!

    private var staffAdapter: StaffListAdapter? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val viewModel: StaffListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(MOVIE_ID_KEY) ?: 0
        val isActor = arguments?.getBoolean(IS_ACTOR_KEY) ?: true

        viewModel.getStaffList(id, isActor)

        staffAdapter = StaffListAdapter { staffId ->
            val bundle = bundleOf(ActorPageFragment.ID_KEY to staffId)
            if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
                findNavController().navigate(R.id.action_staffListFragment_to_actorPageFragment, bundle)
            else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
                findNavController().navigate(R.id.action_staffListFragment2_to_actorPageFragment2, bundle)
            else
                findNavController().navigate(R.id.action_staffListFragment3_to_actorPageFragment3, bundle)
        }

        binding.toolbarActorList.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                    ResourcesCompat.getDrawable(resources, R.drawable.divider, null)?.let { setDrawable(it) }
                }
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = staffAdapter
        }

        mainScope.launch {
            viewModel.staff.collect {
                staffAdapter?.setData(it)
            }
        }

        if (isActor)
            binding.toolbarActorList.tittleTxt.text = getString(R.string.in_movie)
        else
            binding.toolbarActorList.tittleTxt.text = getString(R.string.worked)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        staffAdapter = null
    }

    companion object {
        const val MOVIE_ID_KEY = "movieIdKey"
        const val IS_ACTOR_KEY = "isActorKey"
    }
}