package com.rafiur.assesmentproject.omdb.data.remote.api

import com.rafiur.assesmentproject.BuildConfig
import com.rafiur.assesmentproject.omdb.domain.models.MovieDetailsResponse
import com.rafiur.assesmentproject.omdb.domain.models.SearchResponseData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/")
    suspend fun searchMovies(
        @Query("s") searchQuery: String,
        @Query("page") page: Int = 1,
        @Query("y") year: String = "2000",
        @Query("type") type: String = "movie",
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): SearchResponseData

    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String = BuildConfig.API_KEY,
        @Query("i") id: String
    ): MovieDetailsResponse
}
//@Query("y") year: String = "2020",