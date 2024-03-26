package com.rafiur.assesmentproject.omdb.domain.usecase

import com.rafiur.assesmentproject.omdb.domain.repository.Repository
import com.rafiur.assesmentproject.omdb.domain.datawrapper.ResponseWrapper
import com.rafiur.assesmentproject.omdb.domain.models.MovieDetailsResponse
import com.rafiur.assesmentproject.omdb.domain.models.SearchResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UseCase @Inject constructor(private val repository: Repository) {


    suspend fun getMovies(
        searchQuery: String,
        page: Int,
        year: String = "2000"
    ): Flow<ResponseWrapper<SearchResponseData>> {
        return repository.getAllMovieFromAPI(searchQuery, page, year).map {
            when (it) {
                is ResponseWrapper.GenericError -> {
                    ResponseWrapper.GenericError(it.code, it.error)
                }

                ResponseWrapper.NetworkError -> {
                    ResponseWrapper.NetworkError
                }

                is ResponseWrapper.Success -> {
                    ResponseWrapper.Success(it.value)
                }
            }
        }
    }

    suspend fun getMovieDetail(
        id: String
    ): Flow<ResponseWrapper<MovieDetailsResponse>> {
        return repository.getMovieDetail(id = id).map {
            when (it) {
                is ResponseWrapper.GenericError -> {
                    ResponseWrapper.GenericError(it.code, it.error)
                }

                ResponseWrapper.NetworkError -> {
                    ResponseWrapper.NetworkError
                }

                is ResponseWrapper.Success -> {
                    ResponseWrapper.Success(it.value)
                }
            }
        }
    }
}