package com.averito.weather.models

interface IAdapter<T> {
    fun parse(data: String): T
    fun parseArray(data: String): List<T>
}
