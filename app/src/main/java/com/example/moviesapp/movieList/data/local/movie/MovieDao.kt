package com.example.moviesapp.movieList.data.local.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/*
* Defines database interactions
* */
@Dao
interface MovieDao {
    /*
    * Insert or Update
    * will insert its parameters into the db if it does not already exists (checked by primary key)
    * if it already exist, it will update its parameters in the db
    * */
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    // Get movie by id
    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    // Get movie list by category
    @Query("SELECT * FROM movie WHERE category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>
}