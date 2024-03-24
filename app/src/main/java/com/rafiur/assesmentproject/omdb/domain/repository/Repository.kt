package com.rafiur.assesmentproject.omdb.domain.repository

import com.rafiur.assesmentproject.omdb.domain.ResponseWrapper
import com.rafiur.assesmentproject.omdb.domain.models.MovieDetailsResponse
import com.rafiur.assesmentproject.omdb.domain.models.SearchResponseData
import kotlinx.coroutines.flow.Flow


interface Repository {
    // TODO: Add additional code if you need it

    suspend fun getAllMovieFromOther(
        searchQuery: String,
        page: Int,
        year: String = "2000"
    ): Flow<ResponseWrapper<SearchResponseData>>

    suspend fun getMovieDetail(id: String): Flow<ResponseWrapper<MovieDetailsResponse>>
}