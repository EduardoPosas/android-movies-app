package com.example.moviesapp.movieList.data.remote.response

import com.google.gson.annotations.SerializedName

/*
* Data transfer object from movie database list
* */
data class MovieListDto(
    val page: Int,
    val results: List<MovieDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)