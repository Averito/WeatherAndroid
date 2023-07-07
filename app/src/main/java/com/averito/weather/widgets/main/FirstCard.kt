package com.averito.weather.widgets.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.averito.weather.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun FirstCard(
    updateData: () -> Unit,
    icon: String = "//cdn.weatherapi.com/weather/64x64/night/113.png",
    currentTemp: Double,
    maxTemp: Double,
    minTemp: Double,
    city: String,
    status: String
) {
    val currentDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    val icon = "https:$icon"

    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.4f)
        .padding(15.dp), shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.padding(15.dp)) {
            Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = currentDate)
                    AsyncImage(modifier = Modifier
                        .width(40.dp)
                        .height(40.dp), contentScale = ContentScale.Fit, model = icon, contentDescription = "icon")
                }
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = city, fontSize = 25.sp)
                    Text(modifier = Modifier.padding(bottom = 10.dp), text = "$currentTemp ℃", fontSize = 25.sp)
                    Text(status)
                    Text(text = "$minTemp ℃ - $maxTemp ℃")
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { updateData() }) {
                        Image(painter = painterResource(R.drawable.baseline_cloud_sync_24), contentDescription = "Обновить")
                    }
                }
            }
        }
    }
}