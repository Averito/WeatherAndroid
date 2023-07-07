package com.averito.weather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.averito.weather.api.HttpService
import com.averito.weather.models.weather.Weather
import com.averito.weather.models.weather.impl.WeatherAdapter
import com.averito.weather.ui.theme.WeatherTheme
import com.averito.weather.widgets.main.DayItem
import com.averito.weather.widgets.main.FirstCard
import com.averito.weather.widgets.main.HourItem

class MainActivity : ComponentActivity() {
    private val baseUrl = "https://api.weatherapi.com/v1"
    private lateinit var httpService: HttpService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        httpService = HttpService(baseUrl, baseContext)

        setContent {
            MainScreen(httpService)
        }
    }
}

@Composable
private fun MainScreen(httpService: HttpService) {
    val city = remember { mutableStateOf("Камышин") }
    val weather = remember { mutableStateOf(Weather()) }

    fun updateWeatherData() {
        getWeatherData(httpService, city.value, weather)
    }

    updateWeatherData()

    WeatherTheme(content = {
        Column(modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(15.dp)) {
            FirstCard(
                updateData = { updateWeatherData() },
                currentTemp = weather.value.currentDay.currentTempC,
                maxTemp = weather.value.currentDay.maxTempC,
                minTemp = weather.value.currentDay.minTempC,
                city = weather.value.city,
                icon = weather.value.currentDay.icon,
                status = weather.value.currentDay.status,
            )
            if (weather.value.days.isNotEmpty()) {
                LazyRow(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 15.dp)) {

                    itemsIndexed(weather.value.days[0].hours) { _, item ->
                        HourItem(item = item)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                itemsIndexed(weather.value.days) { _, item ->
                    DayItem(item = item)
                }
            }
        }
    })
}

private fun getWeatherData(httpService: HttpService, city: String, weather: MutableState<Weather>) {
    val apiKey = "dbc9d84c201a48fd97f220050230607"
    val weatherAdapter = WeatherAdapter()

    httpService.get("/forecast.json?key=$apiKey&q=$city&days=7&aqi=no&alerts=no&lang=ru", weather) { response ->
        Log.d("RequestSuccess", response)
        weather.value = weatherAdapter.parse(response)
    }
}
