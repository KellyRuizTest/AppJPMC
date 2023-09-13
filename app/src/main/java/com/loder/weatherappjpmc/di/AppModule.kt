package com.loder.weatherappjpmc.di

import com.loder.weatherappjpmc.data.remote.WeatherAPI
import com.loder.weatherappjpmc.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60 * 60, TimeUnit.SECONDS) // /60*30
        .readTimeout(60 * 60, TimeUnit.SECONDS)
        .writeTimeout(60 * 60, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient): WeatherAPI = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build().create(WeatherAPI::class.java)

}