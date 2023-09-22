package com.loder.weatherappjpmc.data.remote

import com.loder.weatherappjpmc.utils.Constants
import javax.inject.Inject

private val TAG = "WeatherRepository"

class WeatherRepository @Inject constructor(private val api: WeatherAPI) {

    suspend fun getForecastWeather(lat: String, lon: String) = api.getForecastWeather(lat, lon, Constants.API_KEY)
    suspend fun getForecastWeatherbyCity(city: String) = api.getForecastWeatherByCity(city, Constants.API_KEY)
    suspend fun getForecastHourly(lat: String, lon: String) = api.getForecastHourly(lat, lon, Constants.API_KEY)
    suspend fun getCurrentWeather(lat: String, lon: String) = api.getCurrentWeather(lat, lon, Constants.API_KEY)

    suspend fun getCurrentWeatherbyCity(city: String) = api.getCurrentWeatherbyCity(city, Constants.API_KEY)
}
