package com.rafiur.assesmentproject.data.datasource

import com.cube.cubeacademy.lib.api.ErrorHandler.NoConnectivityException
import com.cube.cubeacademy.lib.di.IoDispatcher
import com.rafiur.assesmentproject.domain.ResponseWrapper
import com.rafiur.assesmentproject.domain.models.SearchResponseData
import com.rafiur.assesmentproject.domain.remote.api.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DataSource @Inject constructor(
    private val apiService: ApiService, @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getMoviesTest(
        searchQuery: String,
        page: Int
    ): ResponseWrapper<SearchResponseData> {
        return safeApiCall(apiCall = {
            apiService.searchMovies(searchQuery = searchQuery, page = page)
            //searchQuery = searchQuery, page = page

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