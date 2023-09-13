package com.loder.weatherappjpmc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherList
import com.loder.weatherappjpmc.databinding.WeatherLayoutBinding
import com.loder.weatherappjpmc.utils.Constants
import com.loder.weatherappjpmc.utils.ToTimeString
import com.loder.weatherappjpmc.utils.kelvinToCelsius
import com.squareup.picasso.Picasso

class WeatherAdapter(private val weatherList: List<WeatherList>) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WeatherLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.layoutDateTime.text = weatherList.get(position).dt.ToTimeString().toString().substring(0, 5)
        // holder.binding.layoutRainText.text = weatherList.get(position).rain.toString()
        holder.binding.layoutTemp.text = weatherList.get(position).main.temp.kelvinToCelsius().toString() + "°"
        val image = Constants.ICON_URL + "${weatherList.get(position).weather[0].icon}.png"
        Picasso.get().load(image).resize(18,18).onlyScaleDown().into(holder.binding.layoutTempIcon)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    inner class ViewHolder(val binding: WeatherLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
