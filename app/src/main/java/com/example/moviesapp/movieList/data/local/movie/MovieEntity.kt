package com.example.moviesapp.movieList.data.local.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* Entity that represents one row in the sqlite db
* */

@Entity(tableName = "movie")
data class MovieEntity(
    val adult: Boolean,
    @ColumnInfo("backdrop_path")
    val backdropPath: String,
    @ColumnInfo("genre_ids")
    val genreIds: String,
    @ColumnInfo("original_language")
    val originalLanguage: String,
    @ColumnInfo("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @ColumnInfo("poster_path")
    val posterPath: String,
    @ColumnInfo("release_date")
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    @ColumnInfo("vote_average")
    val voteAverage: Double,
    @ColumnInfo("vote_count")
    val voteCount: Int,
    // Added category manually
    val category: String,
    @PrimaryKey
    val id: Int = 0
)
