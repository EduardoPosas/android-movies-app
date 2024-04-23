package com.example.moviesapp.movieList.util

sealed class Screen(val route: String) {
    data object Home : Screen("main")
    data object PopularMovieList : Screen("popular_movie")
    data object UpcomingMovieList : Screen("upcoming_movie")
    data object Details : Screen("Details")
}