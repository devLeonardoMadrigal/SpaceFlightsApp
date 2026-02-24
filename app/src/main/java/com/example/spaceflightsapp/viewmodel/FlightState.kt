package com.example.spaceflightsapp.viewmodel

import com.example.spaceflightsapp.model.FlightResult

sealed class FlightState {
    object Loading : FlightState()
    data class Success(val flights:List<FlightResult>) : FlightState()
    data class Error(val message: String) : FlightState()
}