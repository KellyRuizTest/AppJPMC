package com.loder.weatherappjpmc

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherList
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.loder.weatherappjpmc.adapter.ForecastAdapter
import com.loder.weatherappjpmc.adapter.WeatherAdapter
import com.loder.weatherappjpmc.databinding.ActivityMainBinding
import com.loder.weatherappjpmc.utils.Constants
import com.loder.weatherappjpmc.utils.ToDateTimeString
import com.loder.weatherappjpmc.utils.kelvinToCelsius
import com.loder.weatherappjpmc.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // viewModel
    private lateinit var viewModel: WeatherViewModel

    // Adapters
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var forecastAdapter: ForecastAdapter

    // Recyclers
    private lateinit var weatherRecycler: RecyclerView
    private lateinit var forecastRecycler: RecyclerView

    private lateinit var binding: ActivityMainBinding
    private lateinit var fuseLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBackground(LocalDateTime.now().hour.toString())
        fuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
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

        setWeatherRecyclerView()
        setForecastRecyclerView()
    }

    private fun setForecastRecyclerView() {
        forecastRecycler = binding.weekForecastRv
        forecastRecycler.layoutManager = LinearLayoutManager(this)
        forecastRecycler.setHasFixedSize(true)

        viewModel.observeForecastWeather().observe(this) {
            forecastAdapter = ForecastAdapter(it)
            forecastRecycler.adapter = forecastAdapter
        }
    }

    private fun setWeatherRecyclerView() {
        weatherRecycler = binding.currentForecastRv
        weatherRecycler.layoutManager = LinearLayoutManager(this)
        weatherRecycler.setHasFixedSize(true)

        viewModel.observeCurrentForecastWeather().observe(this) {
            weatherAdapter = WeatherAdapter(it)
            weatherRecycler.adapter = weatherAdapter
        }
    }

    private fun setCurrentWeather(weather: WeatherList) {
        if (weather != null) {
            binding.tempMain.text = weather.main.temp.kelvinToCelsius().toString() + "°"
            binding.descMain.text = weather.weather[0].description
            binding.dateMain.text = weather.dt.ToDateTimeString()
            binding.humidity.text = "${weather.main.humidity}%"
            binding.windSpeed.text = "${weather.wind.speed}km/h"
            binding.txRain.text = "${weather.pop.toInt()}%"
            binding.feelsLike.text = weather.main.feelsLike.kelvinToCelsius().toString() + "°"
            binding.tempMin.text = weather.main.tempMin.kelvinToCelsius().toString() + "°"
            binding.tempMax.text = weather.main.tempMax.kelvinToCelsius().toString() + "°"
            val image = Constants.ICON_URL + "${weather.weather[0].icon}.png"
            Log.d("MainActivity", image)
            Picasso.get().load(image).placeholder(R.drawable.placeholder).resize(120, 120).onlyScaleDown().into(binding.imageMain)
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
                        viewModel.getCurrentWeather(location.latitude.toString(), location.longitude.toString())
                        Toast.makeText(this, "Longitude: $location.latitude.toString() AND Lattitude: $location.longitude.toString()", Toast.LENGTH_SHORT).show()
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
                val longitude = 12.5221251
                val latitude = -70.039939
                viewModel.getCurrentWeather(latitude.toString(), longitude.toString())
            }
        }
    }

    private fun setBackground(hour: String) {
        if (hour.toInt() in 18..19) {
            binding.layoutActivity.setBackgroundResource(R.drawable.sunset)
        } else if (hour.toInt() in 6..7) {
            binding.layoutActivity.setBackgroundResource(R.drawable.sunrise)
        } else if (hour.toInt() in 8..17) {
            binding.layoutActivity.setBackgroundResource(R.drawable.daylight)
        } else {
            binding.layoutActivity.setBackgroundResource(R.drawable.night)
        }
    }
}
