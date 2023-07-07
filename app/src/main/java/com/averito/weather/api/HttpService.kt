package com.averito.weather.api

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import com.android.volley.Request
import com.android.volley.Request.Method
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.averito.weather.models.IAdapter

class HttpService {
    private val _baseUrl: String
    private val _queue: RequestQueue

    constructor(baseUrl: String, context: Context) {
        _baseUrl = baseUrl
        _queue = Volley.newRequestQueue(context)
    }

    fun <T> get(url: String, state: MutableState<T>, onResponse: (response: String) -> Unit) {
        request<T>(url, Method.GET, state, onResponse)
    }

    private fun <T> request(url: String, method: Int, state: MutableState<T>, onResponse: (response: String) -> Unit) {
        val request = StringRequest(method, "$_baseUrl$url",
            onResponse
        ) { error ->
            Log.d("RequestError", error.toString())
        }
        addToRequestQueue(request)
    }

    private fun <T> addToRequestQueue(request: Request<T>) {
        _queue.add(request)
    }
}