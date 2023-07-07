package com.averito.weather.models.weather.impl

import com.averito.weather.models.weather.IWeatherAdapter
import com.averito.weather.models.weather.Weather
import com.averito.weather.models.weather.nested.WeatherCurrentDay
import com.averito.weather.models.weather.nested.WeatherDayItem
import com.averito.weather.models.weather.nested.WeatherWeatherHourItem
import org.json.JSONArray
import org.json.JSONObject

class WeatherAdapter : IWeatherAdapter {
    override fun parse(data: String): Weather {
        val jsonObject = JSONObject(data)
        val locationJSONObject = jsonObject.getJSONObject("location")
        val currentJsonObject = jsonObject.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day")
        val daysJSONArray = jsonObject.getJSONObject("forecast").getJSONArray("forecastday")

        val days = mutableListOf<WeatherDayItem>()

        for (index in 0 until daysJSONArray.length()) {
            val item = daysJSONArray.getJSONObject(index)
            val itemDay = item.getJSONObject("day")
            val itemDayCondition = itemDay.getJSONObject("condition")
            val hourItemJSONArray = item.getJSONArray("hour")

            val hours = mutableListOf<WeatherWeatherHourItem>()

            for (index2 in 0 until hourItemJSONArray.length()) {
                val hourItem = hourItemJSONArray.getJSONObject(index2)
                val hourItemCondition = hourItem.getJSONObject("condition")

                hours.add(WeatherWeatherHourItem(
                    time = hourItem.getString("time"),
                    tempC = hourItem.getDouble("temp_c"),
                    isDay = hourItem.getInt("is_day"),
                    status = toUtf8(hourItemCondition.getString("text")),
                    icon = hourItemCondition.getString("icon")
                ))
            }

            days.add(WeatherDayItem(
                date = item.getString("date"),
                maxTempC = itemDay.getDouble("maxtemp_c"),
                minTempC = itemDay.getDouble("mintemp_c"),
                avgTempC = itemDay.getDouble("avgtemp_c"),
                status = toUtf8(itemDayCondition.getString("text")),
                icon = itemDayCondition.getString("icon"),
                hours = hours
            ))
        }

        return Weather(
            city = toUtf8(locationJSONObject.getString("name")),
            currentDay = WeatherCurrentDay(
                currentTempC = jsonObject.getJSONObject("current").getDouble("temp_c"),
                maxTempC = currentJsonObject.getDouble("maxtemp_c"),
                minTempC = currentJsonObject.getDouble("mintemp_c"),
                status = toUtf8(currentJsonObject.getJSONObject("condition").getString("text")),
                icon = currentJsonObject.getJSONObject("condition").getString("icon")
            ),
            days = days
        )
    }

    override fun parseArray(data: String): List<Weather> {
        val jsonArray = JSONArray(data)
        val weatherItems = mutableListOf<Weather>()

        for (index in 0..jsonArray.length()) {
            weatherItems.add(parse(jsonArray.getJSONArray(index).toString()))
        }

        return weatherItems
    }

    private fun toUtf8(text: String): String {
        return String(text.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
    }
}