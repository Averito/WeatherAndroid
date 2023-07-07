package com.averito.weather.models.weather

import com.averito.weather.models.weather.nested.WeatherCurrentDay
import com.averito.weather.models.weather.nested.WeatherDayItem

data class Weather(
    val city: String = "Камышин",
    val currentDay: WeatherCurrentDay = WeatherCurrentDay(),
    val days: List<WeatherDayItem> = listOf()
)
