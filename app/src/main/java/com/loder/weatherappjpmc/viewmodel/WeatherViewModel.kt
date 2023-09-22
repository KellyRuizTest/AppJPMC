package com.loder.weatherappjpmc.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loder.weatherappjpmc.data.model.WeatherURL
import com.loder.weatherappjpmc.data.remote.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private val TAG = "WeatherViewModel"

@HiltViewModel
class WeatherViewModel
@Inject constructor(
    private val repository: WeatherRepository,
//

) : ViewModel() {

    private val currentWeatherLiveData = MutableLiveData<WeatherURL>()
    private val cityWeatherLiveData = MutableLiveData<String>()

    fun getCurrentWeatherbyCity(city: String) = viewModelScope.launch {
        repository.getCurrentWeatherbyCity(city).let { response ->

            if (response.isSuccessful) {
                val weatherList = response.body()!!
                val city = weatherList.name
                cityWeatherLiveData.value = city

                // currentWeather
                currentWeatherLiveData.value = weatherList
            } else {
                Log.e(TAG, "getWeatherbyCity error: ${response.message()}")
            }
        }
    }

    fun getCurrentWeatherURL(lat: String, lon: String) = viewModelScope.launch {
        repository.getCurrentWeather(lat, lon).let { response ->

            if (response.isSuccessful) {
                val weatherList = response.body()!!
                val city = weatherList.name
                cityWeatherLiveData.value = city

                // currentWeather
                currentWeatherLiveData.value = weatherList
            } else {
                Log.e(TAG, "getCurrentWeather error: ${response.message()}")
            }
        }
    }

    fun observeCurrentWeather(): LiveData<WeatherURL> {
        return currentWeatherLiveData
    }

    fun observeCurrentCity(): LiveData<String> {
        return cityWeatherLiveData
    }
}
