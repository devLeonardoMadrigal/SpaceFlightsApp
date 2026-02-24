package com.example.spaceflightsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.spaceflightsapp.model.FlightResult
import com.example.spaceflightsapp.ui.theme.SpaceFlightsAppTheme
import com.example.spaceflightsapp.viewmodel.FlightState
import com.example.spaceflightsapp.viewmodel.FlightViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceFlightsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {TopAppBar(title={Text("Space flights Articles", fontWeight = FontWeight(800))})}
                ) { innerPadding ->
                    FlightScreen(
                        viewModel = viewModel(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}




@Composable
fun FlightScreen(viewModel: FlightViewModel, modifier: Modifier = Modifier){
    //create repo and viewmodel object

    val state by viewModel.flightState.observeAsState(FlightState.Loading)

    Box(
        modifier = Modifier
            .fillMaxSize().padding(top = 100.dp),
        contentAlignment = Alignment.Center
    ){
        when(state){
            is FlightState.Error ->{}
            FlightState.Loading -> CircularProgressIndicator()
            is FlightState.Success -> {
                LazyColumn(contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp))
                {
                    items((state as FlightState.Success).flights){ flights ->
                        FlightItems(flights)
                    }
                }
            }
        }
    }
}

@Composable
fun FlightItems(flight: FlightResult) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable{
                println(flight.id)
            },
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(end = 10.dp)) {
                AsyncImage(
                    model = flight.image_url,
                    contentDescription = flight.summary,
                    modifier = Modifier.size(100.dp)
                )
            }
            Column() {
                Row() {
                    Text(
                        text = "Article ${flight.id}",
                        fontWeight = FontWeight(800)
                    )
                }
                Row() {
                    Text(
                        text = flight.title
                    )
                }

            }
        }
    }
}


@Composable
fun FlightDetails(flight: FlightResult){


}