package com.averito.weather.models.weather.nested

import java.time.LocalDateTime

data class WeatherCurrentDay(
    val date: String = LocalDateTime.now().toString(),
    val currentTempC: Double = 0.0,
    val maxTempC: Double = 0.0,
    val minTempC: Double = 0.0,
    val status: String = "Sunny",
    val icon: String = "//cdn.weatherapi.com/weather/64x64/night/113.png"
)