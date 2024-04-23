package com.example.moviesapp.movieList.data.repository

import com.example.moviesapp.movieList.data.local.movie.MovieDatabase
import com.example.moviesapp.movieList.data.mappers.toMovie
import com.example.moviesapp.movieList.data.mappers.toMovieEntity
import com.example.moviesapp.movieList.data.remote.MovieServiceApi
import com.example.moviesapp.movieList.domain.model.Movie
import com.example.moviesapp.movieList.domain.repository.MovieListRepository
import com.example.moviesapp.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieServiceApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            // Set loading state to true
            emit(Resource.Loading(true))

            // Query the local database
            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)
            // if exist data in the local database and is not force fetching
            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote
            if (shouldLoadLocalMovie) {
                emit(
                    Resource.Success(
                        data = localMovieList.map { movieEntity ->
                            movieEntity.toMovie(category)
                        }
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            // if no data is in the local database, request the api service
            val movieListFromApi = try {
                movieApi.getMovieList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            // Map the list movieDto to database entity
            val movieEntities = movieListFromApi.results.let {
                it.map { movieDto -> movieDto.toMovieEntity(category) }
            }

            // Save into the local database
            movieDatabase.movieDao.upsertMovieList(movieEntities)
            // get movie list from local database and mapping to movie model
            emit(Resource.Success(
                movieEntities.map { movieEntity -> movieEntity.toMovie(category) }
            ))

            // isLoading set to false
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDao.getMovieById(id)
            if (movieEntity.id > 0) {
                emit(Resource.Success(movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error("Movie not found"))
        }
    }
}