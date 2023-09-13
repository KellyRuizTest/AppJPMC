package com.loder.weatherappjpmc.data.remote

import com.example.weatherapp.data.model.Forecast
import retrofit2.Response

interface MainRepository {

    suspend fun getCurrentWeather(lat: String, lon: String, appid: String): Response<Forecast>
}
