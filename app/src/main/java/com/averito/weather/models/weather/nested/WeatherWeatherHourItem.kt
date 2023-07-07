package com.averito.weather.models.weather.nested

data class WeatherWeatherHourItem(
    val time: String = "",
    val tempC: Double = 0.0,
    val isDay: Int = 0,
    val status: String = "Sunny",
    val icon: String = "//cdn.weatherapi.com/weather/64x64/night/113.png",
)
