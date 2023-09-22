package com.loder.weatherappjpmc.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherList
import com.loder.weatherappjpmc.data.remote.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private val TAG = "ForeCastViewModel"

@HiltViewModel
class ForecastViewModel
@Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val forecastDayLiveData = MutableLiveData<List<WeatherList>>()
    private val forecast3hourLiveData = MutableLiveData<List<WeatherList>>()

    // if API pro LiveData below
    private val forecastHourlyLiveData = MutableLiveData<List<WeatherList>>()

    fun getForecastWeather(lat: String, lon: String) = viewModelScope.launch {
        val todayForecast = mutableListOf<WeatherList>()
        val forecastWeather = mutableListOf<WeatherList>()
        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        repository.getForecastWeather(lat, lon).let { response ->

            if (response.isSuccessful) {
                val weatherList = response.body()!!.list

                // today forecast are the next six weather forecast
                for (i in 0..5) {
                    todayForecast.add(weatherList[i])
                    println(weatherList[i].dtTxt)
                }
                // forecast3hour store the next six weather forecast
                forecast3hourLiveData.postValue(todayForecast)
                var date: String
                var time: String
                val current_date = weatherList[0].dtTxt.substring(1, 10)
                val current_time = weatherList[0].dtTxt.substring(11, 16)

                // forecast for the next 5 days
                for (j in 2 until (weatherList.size)) {
                    // find weather for the current time in the next days
                    date = weatherList[j].dtTxt.substring(1, 10)
                    time = weatherList[j].dtTxt.substring(11, 16)

                    // store the forecast in the current time to store the forecast in the exact time
                    if (current_time == time) {
                        println(weatherList[j])
                        forecastWeather.add(weatherList[j])
                    }
                }
                forecastWeather.add(weatherList[weatherList.size - 1])
                // forecastDay store forecast for the next 5 days in the exact time that
                forecastDayLiveData.postValue(forecastWeather)
            } else {
                Log.e(TAG, "getForecastWeather error: ${response.message()}")
            }
        }
    }

    fun getForecastWeatherByCity(city: String) = viewModelScope.launch {
        val todayForecast = mutableListOf<WeatherList>()
        val forecastWeather = mutableListOf<WeatherList>()

        repository.getForecastWeatherbyCity(city).let { response ->

            if (response.isSuccessful) {
                val weatherList = response.body()!!.list

                // today forecast are the next six weather forecast
                for (i in 0..6) {
                    todayForecast.add(weatherList[i])
                    println(weatherList[i].dtTxt)
                }
                // forecast3hour store the next six weather forecast
                forecast3hourLiveData.postValue(todayForecast)
                var date: String
                var time: String
                val current_date = weatherList[0].dtTxt.substring(1, 10)
                val current_time = weatherList[0].dtTxt.substring(11, 16)

                // forecast for the next 5 days
                forecastWeather.add(weatherList[1])
                for (j in 2 until (weatherList.size)) {
                    // find weather for the current time in the next days
                    date = weatherList[j].dtTxt.substring(1, 10)
                    time = weatherList[j].dtTxt.substring(11, 16)

                    // store the forecast in the current time to store the forecast in the exact time
                    if (current_date != date && current_time == time) {
                        println(weatherList[j])
                        forecastWeather.add(weatherList[j])
                    }
                }
                forecastWeather.add(weatherList[weatherList.size - 1])
                // forecastDay store forecast for the next 5 days in the exact time that
                forecastDayLiveData.postValue(forecastWeather)
            } else {
                Log.e(TAG, "getForecastWeatherByCity error: ${response.message()}")
            }
        }
    }

    // if API pro function below
    fun getForecastHourly(lat: String, lon: String) = viewModelScope.launch {
        val forecastWeather = mutableListOf<WeatherList>()

        repository.getForecastHourly(lat, lon).let { response ->

            if (response.isSuccessful) {
                val weatherList = response.body()!!.list

                var date: String
                var time: String
                val current_date = weatherList[0].dtTxt.substring(1, 10)
                val current_time = weatherList[0].dtTxt.substring(11, 16)

                for (j in 0 until (weatherList.size)) {
                    // find weather for the current time in the next days
                    date = weatherList[j].dtTxt.substring(1, 10)
                    time = weatherList[j].dtTxt.substring(11, 16)

                    if (current_date == date && current_time != time) {
                        println(weatherList[j])
                        forecastWeather.add(weatherList[j])
                    }
                }
                forecastWeather.add(weatherList[weatherList.size - 1])
                forecastHourlyLiveData.postValue(forecastWeather)
            } else {
                Log.e(TAG, "getForecastHourly error: ${response.message()} -> ${response.body()}")
            }
        }
    }

    fun observeHourlyForecast(): LiveData<List<WeatherList>> {
        return forecast3hourLiveData
    }

    fun observeDayForecast(): LiveData<List<WeatherList>> {
        return forecastDayLiveData
    }
}
