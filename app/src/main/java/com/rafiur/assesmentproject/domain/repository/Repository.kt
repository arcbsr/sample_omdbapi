package com.cube.cubeacademy.lib.repository

import com.rafiur.assesmentproject.domain.ResponseWrapper
import com.rafiur.assesmentproject.domain.models.SearchResponseData
import kotlinx.coroutines.flow.Flow


interface Repository {
    // TODO: Add additional code if you need it

    suspend fun getAllMovieFromOther(searchQuery: String, page: Int): Flow<ResponseWrapper<SearchResponseData>>
}