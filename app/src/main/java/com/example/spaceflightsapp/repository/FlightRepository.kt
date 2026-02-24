package com.example.spaceflightsapp.repository

import com.example.spaceflightsapp.api.FlightApi
import com.example.spaceflightsapp.model.FlightResponse
import com.example.spaceflightsapp.model.FlightResult
import com.example.spaceflightsapp.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FlightRepository (private val api: FlightApi = RetrofitClient.api){

    suspend fun getFlights(): Result<List<FlightResult>> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchFlights("json")
            val flights = response.results
            Result.success(flights)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
