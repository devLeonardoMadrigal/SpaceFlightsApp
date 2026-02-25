package com.example.spaceflightsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.Format
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import com.example.spaceflightsapp.model.FlightResult
import com.example.spaceflightsapp.ui.theme.SpaceFlightsAppTheme
import com.example.spaceflightsapp.viewmodel.FlightState
import com.example.spaceflightsapp.viewmodel.FlightViewModel
import okhttp3.internal.format
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceFlightsAppTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {TopAppBar(title={Text("Space flights Articles", fontWeight = FontWeight(800))})}
                ) { innerPadding ->


                    NavHost(
                        navController = navController,
                        startDestination = "FlightScreen"
                    ){
                        composable("FlightScreen"){
                            FlightScreen(
                                viewModel = viewModel(),
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }
                        composable("FlightDetailsScreen/{flightId}"){ backStackEntry ->

                            val flightId = backStackEntry.arguments?.getString("flightId")


                            FlightDetailsScreen(
                                viewModel = viewModel(),
                                navController = navController,
                                flightId = flightId
                            )
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun FlightScreen(viewModel: FlightViewModel, modifier: Modifier = Modifier, navController: NavController){
    //create repo and viewmodel object

    val state by viewModel.flightState.observeAsState(FlightState.Loading)
    println(state .toString())

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
                    items((state as FlightState.Success).flights){ flight ->
                        FlightItems(flight, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun FlightItems(flight: FlightResult, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable{
                navController.navigate(route= "FlightDetailsScreen/${flight.id}")
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
fun FlightDetailsScreen(viewModel:FlightViewModel,
                        flightId: String?, navController: NavController){
    val state by viewModel.flightState.observeAsState(FlightState.Loading)

    Box(modifier = Modifier
        .fillMaxSize().padding(top = 100.dp),
        contentAlignment = Alignment.Center) {
        when(state){
            is FlightState.Error ->{}
            FlightState.Loading -> CircularProgressIndicator()
            is FlightState.Success -> {
                LazyColumn(contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp))
                {
                    items((state as FlightState.Success).flights){ flight ->
                        if(flight.id.toString() == flightId){
                            Text(
                                text="Article #${flight.id}",
                                fontWeight = FontWeight(800),
                                fontSize = 36.sp,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                AsyncImage(
                                    model = flight.image_url,
                                    contentDescription = flight.summary,
                                    modifier = Modifier.size(300.dp).fillMaxWidth()
                                )
                            }
                            Text(
                                text=flight.title,
                                fontWeight = FontWeight(800),
                                fontSize = 24.sp,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text(
                                text=flight.summary,
                                fontSize = 24.sp,
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Text(
                                "Published on: ${flight.published_at.substring(0,10)}"
                            )

                            Row() {
                                var finalText = "Author"

                                for(author in flight.authors) {
                                    if(flight.authors.size > 1){
                                        finalText += "s: "
                                        if(
                                            !finalText.contains(author.name)
                                        ){
                                            finalText += ", ${author.name},"
                                        }
                                    }else{
                                        finalText += ": ${author.name}"
                                    }

                                }
                                Text(finalText)

                            }


                            Button(onClick = {navController.navigate("FlightScreen")}) {
                                Text("Go back")
                            }

                        }
                    }
                }

            }
        }
    }
}