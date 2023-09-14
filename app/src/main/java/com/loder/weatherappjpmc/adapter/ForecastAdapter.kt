package com.loder.weatherappjpmc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherList
import com.loder.weatherappjpmc.R
import com.loder.weatherappjpmc.databinding.ForecastLayoutBinding
import com.loder.weatherappjpmc.utils.Constants
import com.loder.weatherappjpmc.utils.ToDateTimeString
import com.loder.weatherappjpmc.utils.kelvinToCelsius
import com.squareup.picasso.Picasso

class ForecastAdapter(private val forecastList: List<WeatherList>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ForecastLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.forecastDay.text = forecastList.get(position).dt.ToDateTimeString().substring(0, 6)
        holder.binding.forecastTempMax.text = forecastList.get(position).main.tempMax.kelvinToCelsius().toString() + "°C"
        holder.binding.forecastTempMin.text = forecastList.get(position).main.tempMin.kelvinToCelsius().toString() + "°C"
        val image = "${forecastList.get(position).weather[0].icon}"
        Picasso.get().load(image).resize(18, 18).onlyScaleDown().into(holder.binding.forecastTempIcon)

        when (image) {
            "01d" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w01d)
            "02d" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w02d)
            "03d" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w03d)
            "04d" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w04d)
            "09d" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w09d)
            "10d" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w10d)
            "11d" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w11d)
            "13d" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w13d)
            "50d" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w50d)
            "01n" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w01n)
            "02n" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w02n)
            "03n" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w03n)
            "04n" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w04n)
            "09n" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w09n)
            "10n" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w10n)
            "11n" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w11n)
            "13n" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w13n)
            "50n" -> holder.binding.forecastTempIcon.setImageResource(R.drawable.w50n)
            else -> {
                holder.binding.forecastTempIcon.setImageResource(R.drawable.placeholder)
            }
        }


    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    inner class ViewHolder(val binding: ForecastLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
