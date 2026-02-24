package com.example.spaceflightsapp.api
import com.example.spaceflightsapp.model.FlightResponse
import com.example.spaceflightsapp.model.FlightResult
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.spaceflightnewsapi.net/v4/articles/?format=json
    interface FlightApi {
    @GET("articles/")


    suspend fun searchFlights(@Query("format") format : String = "json"): FlightResponse
}