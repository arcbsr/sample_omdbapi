package com.rafiur.assesmentproject.domain.remote.api

import com.rafiur.assesmentproject.BuildConfig
import com.rafiur.assesmentproject.domain.models.SearchResponseData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/")
    suspend fun searchMovies(
        @Query("s") searchQuery: String,
        @Query("page") page: Int = 1,
        @Query("y") year: String = "2000",
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): SearchResponseData

}
//@Query("y") year: String = "2020",