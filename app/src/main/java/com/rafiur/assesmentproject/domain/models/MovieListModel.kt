package com.rafiur.assesmentproject.domain.models

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String,
    var pageNumber: Int = 0
)

data class SearchResponseData(
    @SerializedName("Search") val search: List<Movie>,
    @SerializedName("totalResults") val totalResults: String,
    @SerializedName("Response") val response: String
)
