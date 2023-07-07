package com.averito.weather.models.weather.nested

import java.util.Date

data class WeatherDayItem(
    val date: String = Date().toString(),
    val maxTempC: Double = 0.0,
    val minTempC: Double = 0.0,
    val avgTempC: Double = 0.0,
    val status: String = "Sunny",
    val icon: String = "//cdn.weatherapi.com/weather/64x64/night/113.png",
    val hours: List<WeatherWeatherHourItem> = listOf()
)
