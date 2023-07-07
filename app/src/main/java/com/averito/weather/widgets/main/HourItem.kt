package com.averito.weather.widgets.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.averito.weather.models.weather.nested.WeatherWeatherHourItem

@Composable
fun HourItem(item: WeatherWeatherHourItem) {
    val fullIcon = "https:${item.icon}"
    val time = item.time.split(" ")[1]

    ElevatedCard(modifier = Modifier.padding(end = 10.dp)) {
        Box(modifier = Modifier.padding(10.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(modifier = Modifier
                    .width(40.dp).height(40.dp)
                    .padding(bottom = 3.dp), contentScale = ContentScale.Fit, model = fullIcon, contentDescription = "icon")
                Text(modifier = Modifier.padding(bottom = 3.dp), text = time)
                Text(text = "${item.tempC} ℃")
            }
        }
    }
}