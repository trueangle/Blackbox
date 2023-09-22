package com.github.trueangle.blackbox.sample.movie.shared.data.api

import com.github.truangle.blackbox.movieapp.data.response.GenreListResponse
import com.github.truangle.blackbox.movieapp.data.response.MovieDetailsResponse
import com.github.truangle.blackbox.movieapp.data.response.MovieListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class MoviesApi(private val httpClient: HttpClient) {

    suspend fun getGenres(): GenreListResponse =
        httpClient.get(BASE_URL + "genre/movie/list?api_key=$API_KEY").body()

    suspend fun getPerPage(page: Int): MovieListResponse =
        httpClient.get(BASE_URL + "movie/now_playing?page=$page&api_key=$API_KEY").body()

    suspend fun getTrending(): MovieListResponse =
        httpClient.get(BASE_URL + "trending/movie/week?api_key=$API_KEY").body()

    suspend fun getPopular(): MovieListResponse =
        httpClient.get(BASE_URL + "movie/popular?api_key=$API_KEY").body()

    suspend fun getTopRated(): MovieListResponse =
        httpClient.get(BASE_URL + "movie/top_rated?api_key=$API_KEY").body()

    suspend fun getTopRatedTVShows(): MovieListResponse =
        httpClient.get(BASE_URL + "tv/top_rated?api_key=$API_KEY").body()

    suspend fun getTrendingTVShows(): MovieListResponse =
        httpClient.get(BASE_URL + "trending/tv/week?api_key=$API_KEY").body()

    suspend fun getMovieDetailsById(movieId: String): MovieDetailsResponse =
        httpClient.get(BASE_URL + "movie/$movieId?api_key=$API_KEY").body()

    suspend fun getSimilarMovies(movieId: String): MovieListResponse =
        httpClient.get(BASE_URL + "movie/${movieId}/similar?api_key=$API_KEY").body()

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val API_KEY = "852eb333fbdf1f20f7da454df993da34"
    }
}