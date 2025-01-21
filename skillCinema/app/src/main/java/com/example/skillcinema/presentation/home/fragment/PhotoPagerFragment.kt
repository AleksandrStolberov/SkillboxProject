package com.example.skillcinema.presentation.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.skillcinema.databinding.FragmentPhotoPagerBinding
import com.example.skillcinema.presentation.home.recyclerview.PhotoViewPagerAdapter

class PhotoPagerFragment : Fragment() {

    private var _binding: FragmentPhotoPagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var photoAdapter: PhotoViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoPagerBinding.inflate(inflater, container, false)
        photoAdapter = PhotoViewPagerAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val srcList = arguments?.getStringArrayList(SRC_KEY)?.toList() ?: emptyList()

        binding.photoViewPager.adapter = photoAdapter
        photoAdapter.setData(srcList)
        arguments?.getInt(INDEX_KEY)?.let { binding.photoViewPager.setCurrentItem(it, false) }

    }

    companion object {
        const val INDEX_KEY = "index"
        const val SRC_KEY = "src"
    }
}