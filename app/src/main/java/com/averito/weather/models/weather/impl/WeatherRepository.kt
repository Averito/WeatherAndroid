package com.averito.weather.models.weather.impl

import com.averito.weather.models.weather.IWeatherRepository
import com.averito.weather.models.weather.Weather

class WeatherRepository : IWeatherRepository {
    private val _items: MutableList<Weather> = mutableListOf<Weather>()

    override val items: List<Weather>
        get() = _items

    override fun getItemByIndex(id: Int): Weather {
        return _items[id]
    }
}
