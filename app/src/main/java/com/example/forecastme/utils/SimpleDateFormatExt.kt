package com.example.forecastme.utils

import java.text.SimpleDateFormat
import java.util.*

fun SimpleDateFormat.formatMillis(timestampMillis: Long): String = format(Date(timestampMillis))