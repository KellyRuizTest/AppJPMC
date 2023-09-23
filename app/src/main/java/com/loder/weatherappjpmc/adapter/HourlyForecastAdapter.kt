package com.loder.weatherappjpmc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherList
import com.loder.weatherappjpmc.R
import com.loder.weatherappjpmc.databinding.WeatherLayoutBinding
import com.loder.weatherappjpmc.utils.ToTimeString
import com.loder.weatherappjpmc.utils.kelvinToCelsius
import com.squareup.picasso.Picasso

class HourlyForecastAdapter(private val weatherList: List<WeatherList>) : RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WeatherLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.layoutDateTime.text = weatherList.get(position).dt.ToTimeString().toString().substring(0, 8)
        holder.binding.layoutTemp.text = weatherList.get(position).main.temp.kelvinToCelsius().toString() + "°C"
        val rainp = if (weatherList.get(position).rain == null) "0%" else "${weatherList.get(position).rain.h}"
        holder.binding.layoutRainText.text = rainp
        val image = "${weatherList.get(position).weather[0].icon}"
        Picasso.get().load(image).resize(18, 18).onlyScaleDown().into(holder.binding.layoutTempIcon)

        when (image) {
            "01d" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w01d)
            "02d" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w02d)
            "03d" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w03d)
            "04d" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w04d)
            "09d" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w09d)
            "10d" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w10d)
            "11d" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w11d)
            "13d" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w13d)
            "50d" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w50d)
            "01n" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w01n)
            "02n" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w02n)
            "03n" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w03n)
            "04n" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w04n)
            "09n" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w09n)
            "10n" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w10n)
            "11n" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w11n)
            "13n" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w13n)
            "50n" -> holder.binding.layoutTempIcon.setImageResource(R.drawable.w50n)
            else -> {
                holder.binding.layoutTempIcon.setImageResource(R.drawable.placeholder)
            }
        }
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    inner class ViewHolder(val binding: WeatherLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
