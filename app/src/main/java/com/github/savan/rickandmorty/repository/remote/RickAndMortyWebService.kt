package com.github.savan.rickandmorty.repository.remote

import com.github.savan.rickandmorty.repository.data.CharacterPage
import com.github.savan.rickandmorty.repository.data.Location
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service end point for Rick and Morty REST API
 */
interface RickAndMortyWebService {
    companion object {
        private const val KEY_PAGE = "page"
        private const val KEY_NAME = "name"
        private const val KEY_LOCATION_ID = "location_id"
    }

    @GET("character")
    fun getCharactersForPage(@Query(KEY_PAGE) page: Int, @Query(KEY_NAME) name: String): Call<CharacterPage?>

    @GET("location/{$KEY_LOCATION_ID}")
    fun getLocation(@Path(KEY_LOCATION_ID) locationId: Int): Call<Location?>
}