package com.averito.weather

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.averito.weather.api.HttpService
import com.averito.weather.models.weather.Weather
import com.averito.weather.models.weather.impl.WeatherAdapter
import com.averito.weather.ui.theme.WeatherTheme
import com.averito.weather.widgets.main.DayItem
import com.averito.weather.widgets.main.FirstCard
import com.averito.weather.widgets.main.HourItem
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private val baseUrl = "https://api.weatherapi.com/v1"
    private lateinit var httpService: HttpService
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var coordinates: MutableState<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        httpService = HttpService(baseUrl, baseContext)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                getCurrentLocation(this, fusedLocationClient, requestPermissionLauncher, coordinates)
            }



        setContent {
            MainScreen(httpService) { city ->
                coordinates = city
                getCurrentLocation(this, fusedLocationClient, requestPermissionLauncher, city)
            }
        }
    }
}

@Composable
private fun MainScreen(httpService: HttpService, updateLocation: (city: MutableState<String>) -> Unit) {
    val scrollState = rememberScrollState()

    val city = remember { mutableStateOf("Камышин") }
    val weather = remember { mutableStateOf(Weather()) }

    fun updateWeatherData() {
        getWeatherData(httpService, city.value, weather)
    }

    updateWeatherData()
    updateLocation(city)

    WeatherTheme(content = {
        Column(modifier = Modifier
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)) {
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
                    .padding(bottom = 15.dp), contentPadding = PaddingValues(start = 15.dp, end = 5.dp)
                ) {

                    itemsIndexed(weather.value.days[0].hours) { _, item ->
                        HourItem(item = item)
                    }
                }
            }

            Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
                for ((_, item) in weather.value.days.withIndex()) {
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

private fun getCurrentLocation(context: Context, fusedLocationClient: FusedLocationProviderClient, requestPermissionLauncher: ActivityResultLauncher<Array<String>>, state: MutableState<String>) {
    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED &&
        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
        requestPermissionLauncher.launch(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION))
        return
    }

    val token = CancellationTokenSource()
    val locationRequest = CurrentLocationRequest.Builder().build()

    val currentLocation = fusedLocationClient.getCurrentLocation(locationRequest, token.token)

    currentLocation.addOnCompleteListener {
        val coordinates = "${it.result.latitude.roundToInt()},${it.result.longitude.roundToInt()}"
        state.value = coordinates

        Log.d("City", coordinates)
    }
}

