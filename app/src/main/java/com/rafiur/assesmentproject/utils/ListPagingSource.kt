//package com.rafiur.assesmentproject.utils
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//
//class ListPagingSource<T : Any>(private val items: List<T>) : PagingSource<Int, T>() {
//
//    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
//        // Since we're not using pagination, return null
//        return null
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
//        try {
//            // Simulate loading the data with a delay
//            // You may replace this with your actual data loading logic
//            val page = params.key ?: 0
//            val data = items
//
//            // Return the data loaded
//            return LoadResult.Page(
//                data = data,
//                prevKey = if (page == 0) null else page - 1,
//                nextKey = null // Since we're not using pagination, nextKey is always null
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }
//}