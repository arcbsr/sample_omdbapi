package com.rafiur.assesmentproject.omdb.domain

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val value: T): ResponseWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null): ResponseWrapper<Nothing>()
    object NetworkError: ResponseWrapper<Nothing>()
}