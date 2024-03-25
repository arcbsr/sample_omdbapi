package com.rafiur.assesmentproject.omdb.domain.repository

import com.rafiur.assesmentproject.omdb.domain.ResponseWrapper
import com.rafiur.assesmentproject.omdb.domain.models.MovieDetailsResponse
import com.rafiur.assesmentproject.omdb.domain.models.SearchResponseData
import kotlinx.coroutines.flow.Flow


interface Repository {

    suspend fun getAllMovieFromAPI(
        searchQuery: String,
        page: Int,
        year: String = "2000"
    ): Flow<ResponseWrapper<SearchResponseData>>

    suspend fun getMovieDetail(id: String): Flow<ResponseWrapper<MovieDetailsResponse>>
}