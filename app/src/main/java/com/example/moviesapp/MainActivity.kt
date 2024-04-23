package com.example.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviesapp.details.presentation.DetailsScreen
import com.example.moviesapp.movieList.util.Screen
import com.example.moviesapp.ui.home.HomeScreen
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

/*
* Hilt can provide dependencies to other Android classes that have
* the @AndroidEntryPoint
* Supported Android Classes:
* Application (@AndroidEntryPoint)
* ViewModel (@HiltViewModel)
* Activity
* Fragment
* View
* Service
* BroadcastReceiver
*
* @AndroidEntryPoint
* generates an individual Hilt component for each Android Class in the project
* this components can receive dependencies from their respective parent classes
* To obtain dependencies from a component, use the @Inject to perform field injection
* */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAppTheme {
                // Changing the bar color
                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(route = Screen.Home.route) {
                            HomeScreen(navController = navController)
                        }
                        composable(
                            route = Screen.Details.route + "/{movie_id}",
                            arguments = listOf(
                                navArgument(name = "movie_id") { type = NavType.IntType }
                            )
                        ) {
                            DetailsScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SetBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(key1 = color) {
        systemUiController.setSystemBarsColor(color)
    }
}
