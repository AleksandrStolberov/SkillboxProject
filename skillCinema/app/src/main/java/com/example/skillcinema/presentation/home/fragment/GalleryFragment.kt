package com.example.skillcinema.presentation.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentGalleryBinding
import com.example.skillcinema.presentation.home.recyclerview.GalleryDetailAdapter
import com.example.skillcinema.presentation.home.recyclerview.MySpanSizeLookup
import com.example.skillcinema.presentation.home.viewmodel.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private var galleryAdapter: GalleryDetailAdapter? = null

    private val viewModel: GalleryViewModel by viewModels()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(ID_KEY) ?: 0
        val pages = arguments?.getInt(PAGES_KEY) ?: 0

        viewModel.getGalleryInfo(id)

        var info = emptyMap<String, Int?>()

        mainScope.launch {
            viewModel.galleryInfo.collect {
                info = it
            }
        }

        binding.myToolbar.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.myToolbar.tittleTxt.text = resources.getString(R.string.gallery)

        galleryAdapter = GalleryDetailAdapter { position, list ->
            val bundle = bundleOf(PhotoPagerFragment.INDEX_KEY to position, PhotoPagerFragment.SRC_KEY to list)
            findNavController().navigate(R.id.action_galleryFragment_to_photoPagerFragment, bundle)
        }

        with(binding.galleryRecyclerView) {
            layoutManager =
                GridLayoutManager(
                    requireContext(), 2, LinearLayoutManager.VERTICAL, false
                ).apply {
                    spanSizeLookup = MySpanSizeLookup(3, 1, 2)
                }
            addItemDecoration(getDivider(DividerItemDecoration.VERTICAL))
            addItemDecoration(getDivider(DividerItemDecoration.HORIZONTAL))
            setHasFixedSize(true)
            adapter = galleryAdapter
        }

        binding.chipGroup.getChildAt(0).setOnClickListener {
            mainScope.launch {
                viewModel.getGallery(id, "STILL", pages).collect {
                    galleryAdapter?.submitData(it)
                }
            }
        }

        binding.chipGroup.getChildAt(0).performClick()

        binding.chipGroup.getChildAt(1).setOnClickListener {
            mainScope.launch {
                viewModel.getGallery(id, "SHOOTING", info["SHOOTING"] ?: 0).collect {
                    galleryAdapter?.submitData(it)
                }
            }
        }

        binding.chipGroup.getChildAt(2).setOnClickListener {
            mainScope.launch {
                viewModel.getGallery(id, "POSTER", info["POSTER"] ?: 0).collect {
                    galleryAdapter?.submitData(it)
                }
            }
        }

    }

    private fun getDivider(orientation: Int): DividerItemDecoration {
        return DividerItemDecoration(binding.root.context, orientation).apply {
            setDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.divider_picture)!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        galleryAdapter = null
    }

    companion object {
        const val ID_KEY = "id"
        const val PAGES_KEY = "pages"
    }
}