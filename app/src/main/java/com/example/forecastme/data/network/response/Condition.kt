package com.example.forecastme.data.network.response

import com.google.gson.annotations.SerializedName

data class Condition(
    @SerializedName("text")
    val description: String,
    @SerializedName("icon")
    val iconUrl: String
)

