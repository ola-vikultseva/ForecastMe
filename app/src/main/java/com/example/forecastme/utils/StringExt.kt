package com.example.forecastme.utils

fun String.withProtocolIfMissing(): String {
    return if (startsWith("//")) "https:$this" else this
}