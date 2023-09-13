package com.loder.weatherappjpmc.data.remote

import com.loder.weatherappjpmc.utils.Constants
import javax.inject.Inject

private val TAG = "WeatherRepository"

class WeatherRepository @Inject constructor(private val api: WeatherAPI) {

    suspend fun getCurrentWeather(lat: String, lon: String) = api.getCurrentWeather(lat, lon, Constants.API_KEY)
    suspend fun getWeatherbyCity(city: String) = api.getWeatherByCity(city, Constants.API_KEY)
}
