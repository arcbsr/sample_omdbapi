package com.rafiur.assessmentproject.omdb.domain.datawrapper

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val value: T): ResponseWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null): ResponseWrapper<Nothing>()
    object NetworkError: ResponseWrapper<Nothing>()
}