package com.example.forecastme.ui.utils

import com.example.forecastme.data.utils.ErrorType

interface ErrorFormatter {
    fun getErrorMessage(error: ErrorType): String
}