package com.example.skillcinema.presentation.home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.skillcinema.MoviesInfo
import com.example.skillcinema.R
import com.example.skillcinema.data.dataSource.database.model.CachedMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.data.dataSource.database.model.ShownMovie
import com.example.skillcinema.databinding.FragmentFilmPageBinding
import com.example.skillcinema.domain.model.AddedMovie
import com.example.skillcinema.domain.model.FilmPage
import com.example.skillcinema.domain.model.FilmPageList
import com.example.skillcinema.presentation.home.recyclerview.GalleryAdapter
import com.example.skillcinema.presentation.home.recyclerview.MovieAdapter
import com.example.skillcinema.presentation.home.recyclerview.StaffAdapter
import com.example.skillcinema.presentation.home.viewmodel.FilmPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmPageFragment : Fragment() {

    private var _binding: FragmentFilmPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilmPageViewModel by viewModels()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var actorAdapter: StaffAdapter? = null
    private var workerAdapter: StaffAdapter? = null
    private var galleryAdapter: GalleryAdapter? = null
    private var movieAdapter: MovieAdapter? = null

    private var addedMovie: AddedMovie = AddedMovie(false, false, false)
    private var movieId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieId = arguments?.getInt(ID_KEY) ?: 0
        viewModel.getAddedMovie(movieId)
        viewModel.getFilm(movieId)
        viewModel.getFilmList(movieId)

        var serialName = ""
        var movie: CollectionMovie? = null

        mainScope.launch {
            viewModel.addedMovie.collect {
                if (it.isNotEmpty())
                    addedMovie = it[0]
                setShownImg(addedMovie)
            }
        }

        mainScope.launch {
            viewModel.film.collect { film ->
                if (film.isNotEmpty()) {
                    serialName = film[0].name
                    setInfo(film[0])
                    movie = movieArgs(film[0], movieId)
                    viewModel.insertCachedMovie(
                        CachedMovie(
                            film[0].id.toInt(),
                            film[0].name,
                            film[0].genres,
                            film[0].poster,
                            film[0].score,
                            film[0].year
                        )
                    )
                }
            }
        }

        actorAdapter = StaffAdapter(
            { staffId -> onClickStaffAdapter(staffId) },
            {}
        )

        workerAdapter = StaffAdapter(
            { staffId -> onClickStaffAdapter(staffId) },
            {}
        )
        galleryAdapter = GalleryAdapter { position -> onClickGalleryAdapter(position) }

        movieAdapter = MovieAdapter(
            { _, _ -> onClickListPageMovieAdapter() },
            { id -> onClickFilmPageMovieAdapter(id) }
        )

        setActorRecyclerView()
        setWorkerRecyclerView()
        setGalleryRecyclerView()
        setSimilarRecyclerView()

        var pages = 0

        mainScope.launch {
            viewModel.filmList.collect {
                if (it.isNotEmpty()) {
                    actorAdapter?.setData(it[0].actors)
                    workerAdapter?.setData(it[0].workers)
                    galleryAdapter?.setData(it[0].gallery.items)
                    pages = it[0].gallery.totalPages ?: 0
                    movieAdapter?.setData(it[0].similarMovie, it[0].similarMovie.size)
                    setCaption(it[0])
                    binding.waitingImg.isVisible = false
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.actorListInfo.allBtn.setOnClickListener { actorInfo(movieId) }

        binding.workerListInfo.allBtn.setOnClickListener { workerInfo(movieId) }

        binding.galleryListInfo.allBtn.setOnClickListener { galleryInfo(pages, movieId) }

        binding.similarListInfo.allBtn.setOnClickListener { similarMovieInfo(movieId) }

        binding.allBtn.setOnClickListener {
            val bundle =
                bundleOf(SeasonsFragment.ID_KEY to movieId, SeasonsFragment.NAME_KEY to serialName)

            if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
                findNavController().navigate(R.id.action_filmPageFragment_to_seasonsFragment, bundle)
            else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
                findNavController().navigate(R.id.action_filmPageFragment2_to_seasonsFragment2, bundle)
            else
                findNavController().navigate(R.id.action_filmPageFragment3_to_seasonsFragment3, bundle)
        }

        binding.tabs.likeBtn.setOnClickListener {
            addedMovie.isLike = if (!addedMovie.isLike) {
                binding.tabs.likeImg.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.like,
                        null
                    )
                )
                mainScope.launch {
                    viewModel.insertCollectionAndMovie(
                        movieId,
                        MoviesInfo.LIKED_ID,
                        movie ?: error("")
                    )
                }

                true
            } else {
                binding.tabs.likeImg.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.no_like,
                        null
                    )
                )
                mainScope.launch {
                    viewModel.removeCollectionAndMovie(movieId, MoviesInfo.LIKED_ID)
                }
                false
            }
        }
        binding.tabs.bookmarkBtn.setOnClickListener {
            addedMovie.isDesired = if (!addedMovie.isDesired) {
                binding.tabs.bookmarkImg.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.bookmark,
                        null
                    )
                )
                mainScope.launch {
                    viewModel.insertCollectionAndMovie(
                        movieId,
                        MoviesInfo.DESIRED_ID,
                        movie ?: error("")
                    )
                }
                true
            } else {
                binding.tabs.bookmarkImg.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.no_bookmark,
                        null
                    )
                )
                mainScope.launch {
                    viewModel.removeCollectionAndMovie(movieId, MoviesInfo.DESIRED_ID)
                }
                false
            }
        }


        binding.tabs.noShownBtn.setOnClickListener {
            addedMovie.isShown = if (!addedMovie.isShown) {
                binding.tabs.shownImg.setImageDrawable(
                    ResourcesCompat.getDrawable(resources, R.drawable.shown, null)
                )
                val shownMovie = ShownMovie(
                    movie?.id ?: 0,
                    movie?.name ?: "",
                    movie?.genre ?: "",
                    movie?.poster ?: "",
                    movie?.score ?: "",
                    movie?.year ?: ""
                )
                mainScope.launch { viewModel.addShownMovie(shownMovie) }
                true
            } else {
                binding.tabs.shownImg.setImageDrawable(
                    ResourcesCompat.getDrawable(resources, R.drawable.no_shown, null)
                )
                mainScope.launch { viewModel.removeShownMovie(movieId) }
                false
            }
        }
        binding.tabs.shareBtn.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://www.kinopoisk.ru/film/$movieId/")
                type = "text/html"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.tabs.otherBtn.setOnClickListener {
            val listener = object: AdditionalBottomSheetFragment.OnActionCompleteListener {
                override fun onActionComplete() {
                    viewModel.getAddedMovie(movieId)
                }
            }
            val dialog = AdditionalBottomSheetFragment().newInstance(movie!!)
            dialog.setOnActionCompleteListener(listener)
            dialog.show(childFragmentManager, null)
        }
    }

    private fun movieArgs(film: FilmPage, movieId: Int): CollectionMovie {
        return CollectionMovie(movieId, film.name, film.genres, film.poster, film.score, film.year)
    }

    private fun setInfo(film: FilmPage) {
        Glide.with(binding.root)
            .load(film.poster)
            .into(binding.posterImageView)

        binding.description.ageTxt.text = film.rating
        binding.description.countryTxt.text = film.countries
        binding.description.genreTxt.text = film.genres
        binding.description.countTxt.text = film.score
        binding.description.movieNameTxt.text = film.name
        binding.description.yearTxt.text = film.year
        binding.description.durationTxt.text = film.filmLength
        binding.movieDescriptionTextView.text = film.description
        binding.movieDescriptionTextView.toggle()

        binding.seriesFrame.isVisible = film.isSerial
        binding.seasonsSeriesTxt.text = film.seasonsAndSeries

    }


    private fun setCaption(film: FilmPageList) {
        binding.actorListInfo.captionTxt.text = resources.getString(R.string.in_movie)
        film.actors.size.toString().also { binding.actorListInfo.allTxt.text = it }
        binding.actorListInfo.nextImg.isVisible = true
        binding.workerListInfo.captionTxt.text = resources.getString(R.string.worked)
        film.workers.size.toString().also { binding.workerListInfo.allTxt.text = it }
        binding.workerListInfo.nextImg.isVisible = true
        binding.galleryListInfo.captionTxt.text = resources.getString(R.string.gallery)
        binding.galleryListInfo.allTxt.text = resources.getString(R.string.all)
        binding.galleryListInfo.nextImg.isVisible = true
        binding.similarListInfo.captionTxt.text = resources.getString(R.string.similar)
        film.similarMovie.size.toString().also { binding.similarListInfo.allTxt.text = it }
        binding.similarListInfo.nextImg.isVisible = true
    }

    private fun actorInfo(movieId: Int) {
        val bundle = bundleOf(
            StaffListFragment.MOVIE_ID_KEY to movieId,
            StaffListFragment.IS_ACTOR_KEY to true
        )
        if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
            findNavController().navigate(R.id.action_filmPageFragment_to_staffListFragment, bundle)
        else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
            findNavController().navigate(R.id.action_filmPageFragment2_to_staffListFragment2, bundle)
        else
            findNavController().navigate(R.id.action_filmPageFragment3_to_staffListFragment3, bundle)
    }

    private fun workerInfo(movieId: Int) {
        val bundle = bundleOf(
            StaffListFragment.MOVIE_ID_KEY to movieId,
            StaffListFragment.IS_ACTOR_KEY to false
        )
        if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
            findNavController().navigate(R.id.action_filmPageFragment_to_staffListFragment, bundle)
        else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
            findNavController().navigate(R.id.action_filmPageFragment2_to_staffListFragment2, bundle)
        else
            findNavController().navigate(R.id.action_filmPageFragment3_to_staffListFragment3, bundle)
    }

    private fun galleryInfo(pages: Int, movieId: Int) {
        val bundle = bundleOf(GalleryFragment.ID_KEY to movieId, GalleryFragment.PAGES_KEY to pages)
        if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
            findNavController().navigate(R.id.action_filmPageFragment_to_galleryFragment, bundle)
        else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
            findNavController().navigate(R.id.action_filmPageFragment2_to_galleryFragment2, bundle)
        else
            findNavController().navigate(R.id.action_filmPageFragment3_to_galleryFragment3, bundle)
    }

    private fun similarMovieInfo(movieId: Int) {
        val bundle = bundleOf(
            ListPageFragment.ID_KEY to movieId,
            ListPageFragment.NAME_KEY to resources.getString(R.string.similar)
        )
        if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
            findNavController().navigate(R.id.action_filmPageFragment_to_listPageFragment, bundle)
        else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
            findNavController().navigate(R.id.action_filmPageFragment2_to_listPageFragment2, bundle)
        else
            findNavController().navigate(R.id.action_filmPageFragment3_to_listPageFragment3, bundle)
    }

    private fun setActorRecyclerView() {
        with(binding.actorRecyclerView) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(binding.root.context, 4).apply {
                orientation = GridLayoutManager.HORIZONTAL
            }
            adapter = actorAdapter

        }
    }

    private fun setWorkerRecyclerView() {
        with(binding.workerRecyclerView) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(binding.root.context, 2).apply {
                orientation = GridLayoutManager.HORIZONTAL
            }
            adapter = workerAdapter
        }
    }

    private fun setGalleryRecyclerView() {
        with(binding.galleryRecyclerView) {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val divider =
                DividerItemDecoration(binding.root.context, DividerItemDecoration.HORIZONTAL)
            addItemDecoration(divider)
            adapter = galleryAdapter
        }
    }

    private fun setSimilarRecyclerView() {
        with(binding.similarMovieRecyclerView) {
            setHasFixedSize(true)
            val divider =
                DividerItemDecoration(binding.root.context, DividerItemDecoration.HORIZONTAL)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(binding.root.context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = movieAdapter
        }
    }

    private fun onClickGalleryAdapter(position: Int) {
        val srcList = viewModel.filmList.value[0].gallery.items.map {
            it.imageUrl
        }
        val bundle = bundleOf(
            PhotoPagerFragment.INDEX_KEY to position,
            PhotoPagerFragment.SRC_KEY to srcList
        )
        if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
            findNavController().navigate(R.id.action_filmPageFragment_to_photoPagerFragment, bundle)
        else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
            findNavController().navigate(R.id.action_filmPageFragment2_to_photoPagerFragment2, bundle)
        else
            findNavController().navigate(R.id.action_filmPageFragment3_to_photoPagerFragment3, bundle)
    }

    private fun onClickListPageMovieAdapter() {
        val bundle =
            bundleOf(
                ListPageFragment.ID_KEY to id,
                ListPageFragment.NAME_KEY to resources.getString(R.string.similar)
            )
        if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
            findNavController().navigate(R.id.action_filmPageFragment_to_listPageFragment, bundle)
        else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
            findNavController().navigate(R.id.action_filmPageFragment2_to_listPageFragment2, bundle)
        else
            findNavController().navigate(R.id.action_filmPageFragment3_to_listPageFragment3, bundle)

    }

    private fun onClickFilmPageMovieAdapter(movieId: Int) {
        val bundle = Bundle()
        bundle.putInt(ID_KEY, movieId)
        if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
            findNavController().navigate(R.id.action_filmPageFragment_self, bundle)
        else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
            findNavController().navigate(R.id.action_filmPageFragment2_self, bundle)
        else
            findNavController().navigate(R.id.action_filmPageFragment3_self, bundle)
    }

    private fun onClickStaffAdapter(staffId: Int) {
        val bundle = bundleOf(ActorPageFragment.ID_KEY to staffId)
        if (findNavController().graph.label.toString() == resources.getString(R.string.nav_home_name))
            findNavController().navigate(R.id.action_filmPageFragment_to_actorPageFragment, bundle)
        else if (findNavController().graph.label.toString() == resources.getString(R.string.nav_search_name))
            findNavController().navigate(R.id.action_filmPageFragment2_to_actorPageFragment2, bundle)
        else
            findNavController().navigate(R.id.action_filmPageFragment3_to_actorPageFragment3, bundle)

    }

    private fun setShownImg(added: AddedMovie) {
        binding.tabs.shownImg.setImageDrawable(
            if (added.isShown)
                ResourcesCompat.getDrawable(resources, R.drawable.shown, null)
            else
                ResourcesCompat.getDrawable(resources, R.drawable.no_shown, null)
        )
        binding.tabs.likeImg.setImageDrawable(
            if (added.isLike)
                ResourcesCompat.getDrawable(resources, R.drawable.like, null)
            else
                ResourcesCompat.getDrawable(resources, R.drawable.no_like, null)
        )
        binding.tabs.bookmarkImg.setImageDrawable(
            if (added.isDesired)
                ResourcesCompat.getDrawable(resources, R.drawable.bookmark, null)
            else
                ResourcesCompat.getDrawable(resources, R.drawable.no_bookmark, null)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        actorAdapter = null
        workerAdapter = null
        galleryAdapter = null
        movieAdapter = null
    }


    companion object {
        const val ID_KEY = "ID"
    }

}