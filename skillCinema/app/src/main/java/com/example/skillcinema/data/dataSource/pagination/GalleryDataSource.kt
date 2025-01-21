package com.example.skillcinema.data.dataSource.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.model.Image
import com.example.skillcinema.data.dataSource.networking.NetworkingModule

class GalleryDataSource(
    val name: String,
    val id: Int
) : PagingSource<Int, Image>() {

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? = 1

    private val network = NetworkingModule

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            network.movieApi().getImagesById(id, name, page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it.items,
                    prevKey = null,
                    nextKey = if (it.items.isEmpty()) null else page + 1
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }

}