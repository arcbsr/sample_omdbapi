package com.rafiur.assesmentproject.omdb.data.repository

import com.rafiur.assesmentproject.omdb.data.datasource.DataSource
import com.rafiur.assesmentproject.omdb.domain.ResponseWrapper
import com.rafiur.assesmentproject.omdb.domain.models.MovieDetailsResponse
import com.rafiur.assesmentproject.omdb.domain.models.SearchResponseData
import com.rafiur.assesmentproject.omdb.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RepositoryImpl @Inject constructor(private val remoteDataSource: DataSource) : Repository {
    override suspend fun getAllMovieFromAPI(
        searchQuery: String,
        page: Int,
        year: String
    ): Flow<ResponseWrapper<SearchResponseData>> {
        return flow {
            emit(remoteDataSource.getMoviesFromAPI(searchQuery, page, year = year))
        }
    }

    override suspend fun getMovieDetail(id: String): Flow<ResponseWrapper<MovieDetailsResponse>> {
        return flow {
            emit(remoteDataSource.getMovieDetail(id))
        }
    }
}