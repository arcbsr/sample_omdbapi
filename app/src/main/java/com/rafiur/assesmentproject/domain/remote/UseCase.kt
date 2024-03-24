package com.rafiur.assesmentproject.domain.remote

import com.cube.cubeacademy.lib.repository.Repository
import com.rafiur.assesmentproject.domain.ResponseWrapper
import com.rafiur.assesmentproject.domain.models.SearchResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UseCase @Inject constructor(private val repository: Repository) {


    suspend fun getMovies(
        searchQuery: String,
        page: Int
    ): Flow<ResponseWrapper<SearchResponseData>> {
        return repository.getAllMovieFromOther(searchQuery, page).map {
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