package com.loder.weatherappjpmc

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.loder.weatherappjpmc.adapter.DayForecastAdapter
import com.loder.weatherappjpmc.adapter.HourlyForecastAdapter
import com.loder.weatherappjpmc.data.model.WeatherURL
import com.loder.weatherappjpmc.databinding.ActivityMainBinding
import com.loder.weatherappjpmc.utils.ToDateTimeString
import com.loder.weatherappjpmc.utils.kelvinToCelsius
import com.loder.weatherappjpmc.viewmodel.ForecastViewModel
import com.loder.weatherappjpmc.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // viewModel
    private lateinit var viewModel: WeatherViewModel
    private lateinit var forecastViewModel: ForecastViewModel

    // Adapters
    private lateinit var forecastDayAdapter: DayForecastAdapter
    private lateinit var forecastHourlyAdapter: HourlyForecastAdapter

    // Recyclers
    private lateinit var forecastDayRecycler: RecyclerView
    private lateinit var forecastHourlyRecycler: RecyclerView

    private lateinit var binding: ActivityMainBinding
    private lateinit var fuseLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setBackground(LocalDateTime.now().hour.toString())
        fuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        forecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
        getCurrentLocation()

        viewModel.observeCurrentWeather().observe(
            this,
        ) {
            setCurrentWeather(it)
        }

        viewModel.observeCurrentCity().observe(
            this,
        ) {
            binding.cityName.text = it.toString()
        }

        setHourlyForecastRecyclerView()
        setDayForecastRecyclerView()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.getCurrentWeatherbyCity(query)
                    forecastViewModel.getForecastWeatherByCity(query)
                    binding.searchView.setQuery("", false)
                    binding.searchView.clearFocus()
                    binding.searchView.isIconified = true
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun setHourlyForecastRecyclerView() {
        forecastHourlyRecycler = binding.currentForecastRv
        forecastHourlyRecycler.layoutManager = LinearLayoutManager(this)
        forecastHourlyRecycler.setHasFixedSize(true)

        forecastViewModel.observeHourlyForecast().observe(this) {
            forecastHourlyAdapter = HourlyForecastAdapter(it)
            forecastHourlyRecycler.adapter = forecastHourlyAdapter
        }
    }

    private fun setDayForecastRecyclerView() {
        forecastDayRecycler = binding.weekForecastRv
        forecastDayRecycler.layoutManager = LinearLayoutManager(this)
        forecastDayRecycler.setHasFixedSize(true)

        forecastViewModel.observeDayForecast().observe(this) {
            forecastDayAdapter = DayForecastAdapter(it)
            forecastDayRecycler.adapter = forecastDayAdapter
        }
    }

    private fun setCurrentWeather(weather: WeatherURL) {
        if (weather != null) {
            binding.tempMain.text = weather.main.temp.kelvinToCelsius().toString() + "°"
            binding.descMain.text = weather.weather[0].description
            binding.dateMain.text = weather.dt.ToDateTimeString()
            binding.humidity.text = "${weather.main.humidity}%"
            binding.windSpeed.text = "${weather.wind.speed}km/h"
            binding.txRain.text = if (weather.rain == null) "0%" else "${weather.rain.h}%"
            binding.feelsLike.text = weather.main.feelsLike.kelvinToCelsius().toString() + "°"
            binding.tempMin.text = weather.main.tempMin.kelvinToCelsius().toString() + "°"
            binding.tempMax.text = weather.main.tempMax.kelvinToCelsius().toString() + "°"
            // val imageUrl = Constants.ICON_URL + "${weather.weather[0].icon}.png"
            // Glide.with(this).load(imageUrl).into(binding.imageMain)
            val img = "${weather.weather[0].icon}"
            setImageWeather(img)
        }
    }

    private fun getCurrentLocation() {
        if (checkLocationPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestLocationPermissions()
                    return
                }
                fuseLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                    } else {
                        viewModel.getCurrentWeatherURL(location.latitude.toString(), location.longitude.toString())
                        forecastViewModel.getForecastWeather(location.latitude.toString(), location.longitude.toString())
                        // if I would have the API pro calling the following fuction, it would be much better to get Hourly Forecast
                        // viewModel.getForecastHourly(location.latitude.toString(), location.longitude.toString())
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        val fineLocationPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        val coarseLocationPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER,
        )
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            100,
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                val city: String = "Madrid"
                viewModel.getCurrentWeatherbyCity(city)
            }
        }
    }

    private fun setBackground(hour: String) {
        if (hour.toInt() in 18..19) {
            binding.layoutActivity.setBackgroundResource(R.drawable.sunset_cardview)
            binding.bgCardviewLayout.setBackgroundResource(R.drawable.sunset)
        } else if (hour.toInt() in 6..7) {
            binding.layoutActivity.setBackgroundResource(R.drawable.sunrise)
            binding.bgCardviewLayout.setBackgroundResource(R.drawable.sunrise_cardview)
        } else if (hour.toInt() in 8..17) {
            binding.layoutActivity.setBackgroundResource(R.drawable.daylight_cardview)
            binding.bgCardviewLayout.setBackgroundResource(R.drawable.daylight_cardview)
            binding.curretForecastCv.strokeColor = resources.getColor(R.color.light_color)
            binding.weekForecastCv.strokeColor = resources.getColor(R.color.light_color)
        } else {
            binding.layoutActivity.setBackgroundResource(R.drawable.night_cardview)
            binding.bgCardviewLayout.setBackgroundResource(R.drawable.night_cardview)
            binding.curretForecastCv.strokeColor = resources.getColor(R.color.night_color)
            binding.weekForecastCv.strokeColor = resources.getColor(R.color.night_color)
        }
    }

    private fun setImageWeather(buildImg: String) {
        when (buildImg) {
            "01d" -> binding.imageMain.setImageResource(R.drawable.w01d)
            "02d" -> binding.imageMain.setImageResource(R.drawable.w02d)
            "03d" -> binding.imageMain.setImageResource(R.drawable.w03d)
            "04d" -> binding.imageMain.setImageResource(R.drawable.w04d)
            "09d" -> binding.imageMain.setImageResource(R.drawable.w09d)
            "10d" -> binding.imageMain.setImageResource(R.drawable.w10d)
            "11d" -> binding.imageMain.setImageResource(R.drawable.w11d)
            "13d" -> binding.imageMain.setImageResource(R.drawable.w13d)
            "50d" -> binding.imageMain.setImageResource(R.drawable.w50d)
            "01n" -> binding.imageMain.setImageResource(R.drawable.w01n)
            "02n" -> binding.imageMain.setImageResource(R.drawable.w02n)
            "03n" -> binding.imageMain.setImageResource(R.drawable.w03n)
            "04n" -> binding.imageMain.setImageResource(R.drawable.w04n)
            "09n" -> binding.imageMain.setImageResource(R.drawable.w09n)
            "10n" -> binding.imageMain.setImageResource(R.drawable.w10n)
            "11n" -> binding.imageMain.setImageResource(R.drawable.w11n)
            "13n" -> binding.imageMain.setImageResource(R.drawable.w13n)
            "50n" -> binding.imageMain.setImageResource(R.drawable.w50n)
            else -> {
                binding.imageMain.setImageResource(R.drawable.placeholder)
            }
        }
    }
}
