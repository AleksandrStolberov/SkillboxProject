package com.example.skillcinema.presentation.home.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.databinding.DialogCollectionBinding
import com.example.skillcinema.databinding.FragmentBottomSheetAddtionalBinding
import com.example.skillcinema.presentation.home.recyclerview.MyCollectionAdapter
import com.example.skillcinema.presentation.home.viewmodel.AdditionalViewModel
import com.example.skillcinema.presentation.home.viewmodel.FilmPageViewModel
import com.example.skillcinema.withArguments
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdditionalBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAddtionalBinding? = null
    private val binding get() = _binding!!

    private var myCollectionAdapter: MyCollectionAdapter? = null

    private val viewModel: AdditionalViewModel by viewModels()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var dialog: AlertDialog? = null

    private lateinit var listener: OnActionCompleteListener

    fun setOnActionCompleteListener(listener: OnActionCompleteListener) {
        this.listener = listener
    }

    interface OnActionCompleteListener {
        fun onActionComplete()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetAddtionalBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie = arguments?.getParcelable(MOVIE, CollectionMovie::class.java) ?: error("")
        myCollectionAdapter = MyCollectionAdapter { flag, id ->
            if (flag) {
                mainScope.launch {
                    viewModel.removeCollectionAndMovie(movie.id, id)
                }
            } else {
                mainScope.launch {
                    viewModel.insertCollectionAndMovie(movie.id, id, movie)
                }
            }
            listener.onActionComplete()
        }


        mainScope.launch {
            viewModel.collectionsByMovieId(movie.id).collect {
                val collections = it.map { item -> item.collectionId }
                myCollectionAdapter?.setCollectionsId(collections)
            }
        }

        with(binding.recyclerView) {
            val divider =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                    ResourcesCompat.getDrawable(resources, com.example.skillcinema.R.drawable.divider, null)?.let { setDrawable(it) }
                }
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = myCollectionAdapter
            setHasFixedSize(true)
        }


        mainScope.launch {
            viewModel.getAllCollections().collect {
                myCollectionAdapter?.setData(it)
            }
        }


        binding.movieItem.nameTxt.text = movie.name
        binding.movieItem.yearTxt.text = movie.year
        binding.movieItem.countTxt.text = movie.score
        binding.movieItem.countTxt.isVisible = movie.score.isNotEmpty()
        binding.movieItem.genreTxt.text = movie.genre
        binding.movieItem.shownBtn.isVisible = false

        Glide.with(binding.root)
            .load(movie.poster)
            .into(binding.movieItem.posterImageView)

        binding.closeBtn.setOnClickListener {
            dismiss()
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
                dialog?.dismiss()
            }
        }
        bindingDialog.closeBtn.setOnClickListener {
            dialog?.dismiss()
        }
        dialog = AlertDialog.Builder(requireContext())
            .setView(bindingDialog.root)
            .show()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        dialog = null
        myCollectionAdapter = null
    }

    companion object {
        const val MOVIE = "movie"
    }

    fun newInstance(movie: CollectionMovie): AdditionalBottomSheetFragment {
        return AdditionalBottomSheetFragment().withArguments {
            putParcelable(MOVIE, movie)
        }
    }

}