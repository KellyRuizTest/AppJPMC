package com.loder.weatherappjpmc.data.model

import com.example.weatherapp.data.model.Clouds
import com.example.weatherapp.data.model.Coord
import com.example.weatherapp.data.model.Main
import com.example.weatherapp.data.model.Sys
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.model.Wind
import com.google.gson.annotations.SerializedName

data class WeatherURL(
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("weather")
    val weather: Weather,
    @SerializedName("base")
    val base: String,
    @SerializedName("main")
    val main: Main,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("wind")
    val wind: Wind,
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("timezone")
    val timezone: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("cod")
    val cod: Int,
)
