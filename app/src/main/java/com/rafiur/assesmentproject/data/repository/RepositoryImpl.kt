package com.rafiur.assesmentproject.data.repository

import com.cube.cubeacademy.lib.repository.Repository
import com.rafiur.assesmentproject.data.datasource.DataSource
import com.rafiur.assesmentproject.domain.ResponseWrapper
import com.rafiur.assesmentproject.domain.models.SearchResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RepositoryImpl @Inject constructor(private val remoteDataSource: DataSource) : Repository {
    override suspend fun getAllMovieFromOther(
        searchQuery: String,
        page: Int
    ): Flow<ResponseWrapper<SearchResponseData>> {
        return flow {
            emit(remoteDataSource.getMoviesTest(searchQuery, page))
        }
    }
}