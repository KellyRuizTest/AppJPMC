package com.loder.weatherappjpmc.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Forecast
import com.example.weatherapp.data.model.WeatherList
import com.loder.weatherappjpmc.data.remote.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private val TAG = "ForeCastViewModel"

@HiltViewModel
class ForecastViewModel
@Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val forecastDayLiveData = MutableLiveData<List<WeatherList>>()
    private val forecast3hourLiveData = MutableLiveData<List<WeatherList>>()
    private val rainToday = MutableLiveData<Double>()
    private val forecast = MutableLiveData<Forecast>()

    // if API pro LiveData below
    private val forecastHourlyLiveData = MutableLiveData<List<WeatherList>>()

    fun getForecastWeather(lat: String, lon: String) = viewModelScope.launch {
        val todayForecast = mutableListOf<WeatherList>()
        val rain = mutableListOf<Double>()

        repository.getForecastWeather(lat, lon).let { response ->

            if (response.isSuccessful) {
                val forecastAll = response.body()!!
                val weatherList = response.body()!!.list

                // today forecast are the next six weather forecast
                for (i in 0..5) {
                    todayForecast.add(weatherList[i])
                    rain.add(weatherList[i].pop)
                }
                forecastAll.list = todayForecast
                forecast.postValue(forecastAll)
                rainToday.value = rain.max()

                weatherList.forEach {
                    println(it)
                }

                // forecast3hour store the next six weather forecast
                forecast3hourLiveData.postValue(todayForecast)

                // sort Array and find maxTemp and minTemp by eachday forecast
                weatherList.sortedBy { it.dtTxt }
                val forecastArray = MinAndMaxArray(weatherList)
                forecastDayLiveData.postValue(forecastArray)
            } else {
                Log.e(TAG, "getForecastWeather error: ${response.message()}")
            }
        }
    }

    fun getForecastWeatherByCity(city: String) = viewModelScope.launch {
        val todayForecast = mutableListOf<WeatherList>()
        val rain = mutableListOf<Double>()

        repository.getForecastWeatherbyCity(city).let { response ->

            if (response.isSuccessful) {
                val forecastAll = response.body()!!
                val weatherList = response.body()!!.list

                // today forecast are the next six weather forecast
                for (i in 0..5) {
                    todayForecast.add(weatherList[i])
                    rain.add(weatherList[i].pop)
                }
                forecastAll.list = todayForecast
                forecast.postValue(forecastAll)
                rainToday.value = rain.max()

                // forecast3hour store the next six weather forecast
                forecast3hourLiveData.postValue(todayForecast)

                // sort Array and find maxTemp and minTemp by eachday forecast
                weatherList.sortedBy { it.dtTxt }
                val forecastArray = MinAndMaxArray(weatherList)
                forecastDayLiveData.postValue(forecastArray)
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

//    fun observeHourlyForecast(): LiveData<List<WeatherList>> {
//        return forecast3hourLiveData
//    }

    fun observeHourlyForecast(): LiveData<Forecast> {
        return forecast
    }

    fun observeDayForecast(): LiveData<List<WeatherList>> {
        return forecastDayLiveData
    }

    private fun MinAndMaxArray(weatherList: List<WeatherList>): List<WeatherList> {
        var dateCheck = weatherList[0].dtTxt.substring(0, 10)
        var maxTemp = weatherList[0].main.tempMax
        var minTemp = weatherList[0].main.tempMin

        val result = mutableListOf<WeatherList>()

        for (i in 1 until weatherList.size) {
            if (dateCheck == weatherList[i].dtTxt.substring(0, 10)) {
                if (weatherList[i].main.tempMax > maxTemp) {
                    maxTemp = weatherList[i].main.tempMax
                }
                if (weatherList[i].main.tempMin < minTemp) {
                    minTemp = weatherList[i].main.tempMin
                }
            } else {
                var weatherObjec = weatherList[i - 1]
                weatherObjec.main.tempMin = minTemp
                weatherObjec.main.tempMax = maxTemp
                result.add(weatherObjec)
                minTemp = weatherList[i].main.tempMin
                maxTemp = weatherList[i].main.tempMax
                dateCheck = weatherList[i].dtTxt.substring(0, 10)
            }
        }

        /*var lastElement = weatherList[weatherList.size - 1]
        lastElement.main.tempMin = minTemp
        lastElement.main.tempMax = maxTemp
        result.add(lastElement)*/
        return result
    }

    fun observeRainToday(): LiveData<Double> {
        return rainToday
    }
}
