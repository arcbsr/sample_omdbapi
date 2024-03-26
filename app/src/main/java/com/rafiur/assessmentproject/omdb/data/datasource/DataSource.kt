package com.rafiur.assessmentproject.omdb.data.datasource

import com.rafiur.assessmentproject.omdb.data.remote.exception.NoConnectivityException
import com.cube.cubeacademy.lib.di.IoDispatcher
import com.rafiur.assessmentproject.omdb.domain.datawrapper.ResponseWrapper
import com.rafiur.assessmentproject.omdb.domain.models.SearchResponseData
import com.rafiur.assessmentproject.omdb.data.remote.api.ApiService
import com.rafiur.assessmentproject.omdb.domain.models.MovieDetailsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DataSource @Inject constructor(
    private val apiService: ApiService, @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getMoviesFromAPI(
        searchQuery: String,
        page: Int,
        year: String = "2000"
    ): ResponseWrapper<SearchResponseData> {
        return safeApiCall(apiCall = {
            apiService.searchMovies(searchQuery = searchQuery, page = page, year = year)

        })
    }

    suspend fun getMovieDetail(id: String): ResponseWrapper<MovieDetailsResponse> {
        return safeApiCall(apiCall = {
            apiService.getMovieDetails(id = id)

        })
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResponseWrapper<T> {
        return withContext(ioDispatcher) {
            try {
                val data = apiCall.invoke()
                ResponseWrapper.Success(data)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is NoConnectivityException -> ResponseWrapper.NetworkError
                    is IOException -> ResponseWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        val msg = throwable.message()
                        val errorMsg = if (msg.isNullOrEmpty()) {
                            throwable.response()?.errorBody()?.string()
                        } else {
                            msg
                        }
                        ResponseWrapper.GenericError(code, errorMsg)
                    }

                    else -> {
                        ResponseWrapper.GenericError(0, throwable.message)
                    }
                }
            }
        }
    }

}