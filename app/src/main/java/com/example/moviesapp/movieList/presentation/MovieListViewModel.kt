package com.example.moviesapp.movieList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.movieList.domain.repository.MovieListRepository
import com.example.moviesapp.movieList.util.Category
import com.example.moviesapp.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val movieListRepository: MovieListRepository) :
    ViewModel() {
    // Define state as StateFlow
    private var _movieListUiState = MutableStateFlow(MovieListUiState())
    val movieListUiState = _movieListUiState.asStateFlow()

    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }

    fun onEvent(event: MovieListUiEvent) {
        when(event) {
            MovieListUiEvent.Navigate -> {
                _movieListUiState.update {
                    it.copy(
                        isCurrentPopularScreen = !movieListUiState.value.isCurrentPopularScreen
                    )
                }
            }
            is MovieListUiEvent.Paginate -> {
                if(event.category == Category.POPULAR) {
                    getPopularMovieList(true)
                } else if(event.category == Category.UPCOMING) {
                    getUpcomingMovieList(true)
                }
            }
        }
    }

    private fun getPopularMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            // set loading ui state to true
            _movieListUiState.update {
                it.copy(
                    isLoading = true
                )
            }

            // Get the popular movie list using the repository
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                movieListUiState.value.popularMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListUiState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            _movieListUiState.update {
                                it.copy(
                                    popularMovieList = movieListUiState.value.popularMovieList + popularList.shuffled(),
                                    popularMovieListPage = movieListUiState.value.popularMovieListPage.plus(1)
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListUiState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            // set loading ui state to true
            _movieListUiState.update {
                it.copy(
                    isLoading = true
                )
            }

            // Get the upcoming movie list using the repository
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.UPCOMING,
                movieListUiState.value.upcomingMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListUiState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { upcomingList ->
                            _movieListUiState.update {
                                it.copy(
                                    upcomingMovieList = movieListUiState.value.upcomingMovieList + upcomingList.shuffled(),
                                    upcomingMovieListPage = movieListUiState.value.upcomingMovieListPage.plus(1)
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListUiState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }
}