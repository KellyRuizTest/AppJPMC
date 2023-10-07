package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("list")
    var list: List<WeatherList>,
    @SerializedName("message")
    val message: Int,
)
