package com.loder.weatherappjpmc.data.remote

import com.loder.weatherappjpmc.utils.Constants
import javax.inject.Inject

private val TAG = "WeatherRepository"

class WeatherRepository @Inject constructor(private val api: WeatherAPI) {
//
//    private lateinit var lat: String
//    private lateinit var lon: String

    suspend fun getCurrentWeather(lat: String, lon: String) = api.getCurrentWeather(lat, lon, Constants.API_KEY)

//    override suspend fun getCurrentWeather(
//        lat: String,
//        lon: String,
//        appid: String,
//    ): Response<Forecast> {
//        val response = api.getCurrentWeather(lat, lon, Constants.API_KEY)
//        val result = response.body()
//        if (response.isSuccessful && result != null) {
//            return response
//        } else {
//            Log.e(TAG, response.message())
//            return response
//        }
//    }
}
