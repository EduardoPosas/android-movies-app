package com.example.moviesapp.movieList.data.remote

import com.example.moviesapp.BuildConfig
import com.example.moviesapp.movieList.data.remote.response.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/*
* Service verbs from the movie database
* */
interface MovieServiceApi {
    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Header("Authorization") authorization: String = "Bearer $ACCESS_TOKEN"
    ): MovieListDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val ACCESS_TOKEN = BuildConfig.ACCESS_TOKEN
    }
}