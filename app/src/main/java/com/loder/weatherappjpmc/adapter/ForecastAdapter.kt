package com.loder.weatherappjpmc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherList
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
        holder.binding.forecastTempMax.text = forecastList.get(position).main.tempMax.kelvinToCelsius().toString() + "°"
        holder.binding.forecastTempMin.text = forecastList.get(position).main.tempMin.kelvinToCelsius().toString() + "°"
        val image = Constants.ICON_URL + "${forecastList.get(position).weather[0].icon}.png"
        Picasso.get().load(image).resize(18, 18).onlyScaleDown().into(holder.binding.forecastTempIcon)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    inner class ViewHolder(val binding: ForecastLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
