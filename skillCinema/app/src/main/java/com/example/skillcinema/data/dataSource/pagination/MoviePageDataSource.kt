package com.example.skillcinema.data.dataSource.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.MoviesInfo
import com.example.skillcinema.data.model.MovieResponse
import com.example.skillcinema.data.model.SearchSettings
import com.example.skillcinema.data.dataSource.networking.NetworkingModule

class MoviePageDataSource(
    private val name: String,
    private val collection: List<Int>?,
    private val settings: SearchSettings? = null
) : PagingSource<Int, MovieResponse>() {

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int = 1

    private val network = NetworkingModule

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            when (name) {
                MoviesInfo.SEARCH -> settings?.let {
                    network.movieApi().getMoviesBySettings(
                        it.country.keys.toIntArray(), it.genre.keys.toIntArray(), it.type, page, it.yearFrom, it.yearTo,
                        it.ratingFrom, it.ratingTo, it.order, it.keyword
                    )
                }?.items!!
                MoviesInfo.TOP_POPULAR -> network.movieApi().getTopMovies("TOP_POPULAR_ALL", page).items
                MoviesInfo.TOP_250 -> network.movieApi().getTopMovies("TOP_250_MOVIES", page).items
                MoviesInfo.TV_SERIES ->
                    network.movieApi().getCollectionMovies(
                        MoviesInfo.COUNTRIES_SERIES, MoviesInfo.GENRES_SERIES, "TV_SERIES", page
                    ).items
                else -> {
                    network.movieApi().getCollectionMovies(
                        intArrayOf(collection?.get(0) ?: 0), intArrayOf(
                            collection?.get(1) ?: 0
                        ), page = page
                    ).items
                }
            }
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }

}