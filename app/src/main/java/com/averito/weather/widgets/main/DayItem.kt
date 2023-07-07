package com.averito.weather.widgets.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.averito.weather.models.weather.nested.WeatherDayItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DayItem(item: WeatherDayItem) {
    val icon = "https:${item.icon}"
    val date = LocalDate.parse(item.date).format(DateTimeFormatter.ofPattern("dd.MM"))

    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 10.dp)) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            Column() {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(modifier = Modifier.padding(end = 10.dp), text = date)
                        AsyncImage(modifier = Modifier
                            .width(25.dp)
                            .height(25.dp), contentScale = ContentScale.FillBounds, model = icon, contentDescription = "icon")
                    }
                    Text(text = item.status, fontSize = 12.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Средняя: ${item.avgTempC} ℃")
                    Text(text = "${item.minTempC} ℃ / ${item.maxTempC} ℃")
                }
            }
        }
    }
}
