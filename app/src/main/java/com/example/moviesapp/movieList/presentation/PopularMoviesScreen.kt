package com.example.moviesapp.movieList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviesapp.movieList.presentation.components.MovieItem
import com.example.moviesapp.movieList.util.Category

@Composable
fun PopularMoviesScreen(
    movieListUiState: MovieListUiState,
    navController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (movieListUiState.popularMovieList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
        ) {
            items(movieListUiState.popularMovieList.size) { index ->
                MovieItem(
                    movie = movieListUiState.popularMovieList[index],
                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (index >= movieListUiState.popularMovieList.size - 1 && !movieListUiState.isLoading) {
                    onEvent(MovieListUiEvent.Paginate(Category.POPULAR))
                }
            }
        }
    }
}