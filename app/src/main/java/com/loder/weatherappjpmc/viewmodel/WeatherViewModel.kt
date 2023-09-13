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

private val TAG = "WeatherViewModel"

@HiltViewModel
class WeatherViewModel
@Inject constructor(
    private val repository: WeatherRepository,
//

) : ViewModel() {
    private val currentWeatherLiveData = MutableLiveData<WeatherList>()
    private val todayWeatherLiveData = MutableLiveData<List<WeatherList>>()
    private val forecastWeatherLiveData = MutableLiveData<List<WeatherList>>()
    private val cityWeatherLiveData = MutableLiveData<String>()

    fun getCurrentWeather(lat: String, lon: String) = viewModelScope.launch {
        val todayForecast = mutableListOf<WeatherList>()
        val forecastWeather = mutableListOf<WeatherList>()
        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        repository.getCurrentWeather(lat, lon).let { response ->

            if (response.isSuccessful) {
                val weatherList = response.body()!!.list
                val city = response.body()!!.city.name
                cityWeatherLiveData.value = city

                // currentWeather
                currentWeatherLiveData.value = weatherList[0]
                // today forecast are the next 6 weather
                for (i in 1..6) {
                    todayForecast.add(weatherList[i])
                    // println(weatherList[i])
                    println(weatherList[i].dtTxt)
                }
                todayWeatherLiveData.postValue(todayForecast)
                var date: String
                var time: String
                val current_date = weatherList[0].dtTxt.substring(1, 10)
                val current_time = weatherList[0].dtTxt.substring(11, 16)
                forecastWeather.add(weatherList[2])
                for (j in 2 until (weatherList.size)) {
                    // find weather for the current time in the next days
                    date = weatherList[j].dtTxt.substring(1, 10)
                    time = weatherList[j].dtTxt.substring(11, 16)

                    if (current_date != date && current_time == time) {
                        println(weatherList[j])
                        forecastWeather.add(weatherList[j])
                    }
                }
                forecastWeather.add(weatherList[weatherList.size - 1])
                forecastWeatherLiveData.postValue(forecastWeather)
            } else {
                Log.e(TAG, "getCurrentWeather error: ${response.message()}")
            }
        }
    }

    fun getCurrentWeatherbyCity(city: String) = viewModelScope.launch {
        val todayForecast = mutableListOf<WeatherList>()
        val forecastWeather = mutableListOf<WeatherList>()
        repository.getWeatherbyCity(city).let { response ->

            if (response.isSuccessful) {
                val weatherList = response.body()!!.list
                val city = response.body()!!.city.name
                cityWeatherLiveData.value = city

                // currentWeather
                currentWeatherLiveData.value = weatherList[0]
                // today forecast are the next 6 weather
                for (i in 1..6) {
                    todayForecast.add(weatherList[i])
                    // println(weatherList[i])
                    println(weatherList[i].dtTxt)
                }
                todayWeatherLiveData.postValue(todayForecast)
                var date: String
                var time: String
                val current_date = weatherList[0].dtTxt.substring(1, 10)
                val current_time = weatherList[0].dtTxt.substring(11, 16)
                forecastWeather.add(weatherList[2])
                for (j in 2 until (weatherList.size)) {
                    // find weather for the current time in the next days
                    date = weatherList[j].dtTxt.substring(1, 10)
                    time = weatherList[j].dtTxt.substring(11, 16)

                    if (current_date != date && current_time == time) {
                        println(weatherList[j])
                        forecastWeather.add(weatherList[j])
                    }
                }
                forecastWeather.add(weatherList[weatherList.size - 1])
                forecastWeatherLiveData.postValue(forecastWeather)
            } else {
                Log.e("getWeatherbyCity error:", "${response.message()}")
            }
        }
    }

    fun observeCurrentWeather(): LiveData<WeatherList> {
        return currentWeatherLiveData
    }

    fun observeCurrentForecastWeather(): LiveData<List<WeatherList>> {
        return todayWeatherLiveData
    }

    fun observeForecastWeather(): LiveData<List<WeatherList>> {
        return forecastWeatherLiveData
    }

    fun observeCurrentCity(): LiveData<String> {
        return cityWeatherLiveData
    }
}
