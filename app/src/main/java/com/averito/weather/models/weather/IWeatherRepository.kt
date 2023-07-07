package com.averito.weather.models.weather

interface IWeatherRepository {
    val items: List<Weather>

    fun getItemByIndex(id: Int): Weather
}