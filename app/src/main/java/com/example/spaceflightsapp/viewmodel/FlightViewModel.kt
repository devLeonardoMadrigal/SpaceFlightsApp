package com.example.spaceflightsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceflightsapp.repository.FlightRepository
import kotlinx.coroutines.launch

class FlightViewModel(private val flightRepository: FlightRepository = FlightRepository()): ViewModel() {

    private val _flightState = MutableLiveData<FlightState>(FlightState.Loading)
    val flightState: LiveData<FlightState> = _flightState

    init {
        fetchFlights()
    }

    private fun fetchFlights() {
        viewModelScope.launch {
            _flightState.value = FlightState.Loading
            val result = flightRepository.getFlights()
            _flightState.value = if(result.isSuccess){
                val flights = result.getOrNull() ?: emptyList()
                FlightState.Success(flights)
            } else{
                FlightState.Error(result.exceptionOrNull() ?.message ?: "Unknown error")
            }
        }
    }
}