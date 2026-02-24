package com.example.spaceflightsapp.remote

import com.example.spaceflightsapp.api.FlightApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object RetrofitClient {
    private const val BASE_URL = "https://api.spaceflightnewsapi.net/v4/"

    val api: FlightApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlightApi::class.java)

    }
}