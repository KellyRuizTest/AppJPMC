package com.loder.weatherappjpmc.data.remote

import com.example.weatherapp.data.model.Forecast
import com.loder.weatherappjpmc.data.model.WeatherURL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("lat")
        lat: String,
        @Query("lon")
        lon: String,
        @Query("appid")
        appid: String,
    ): Response<WeatherURL>

    @GET("weather?")
    suspend fun getCurrentWeatherbyCity(
        @Query("q")
        city: String,
        @Query("appid")
        appid: String,
    ): Response<WeatherURL>

    @GET("forecast?")
    suspend fun getForecastWeather(
        @Query("lat")
        lat: String,
        @Query("lon")
        lon: String,
        @Query("appid")
        appid: String,
    ): Response<Forecast>

    @GET("forecast?")
    suspend fun getForecastWeatherByCity(
        @Query("q")
        city: String,
        @Query("appid")
        appid: String,
    ): Response<Forecast>

    @GET("forecast/hourly")
    suspend fun getForecastHourly(
        @Query("lat")
        lat: String,
        @Query("lon")
        lon: String,
        @Query("appid")
        appid: String,
    ): Response<Forecast>


}
