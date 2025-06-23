package com.example.forecastme.data.model

import com.example.forecastme.data.utils.ErrorType

sealed class ResultWrapper<T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    class Loading<T> : ResultWrapper<T>()
    data class Error<T>(val error: ErrorType) : ResultWrapper<T>()
}