package com.github.savan.rickandmorty.repository

import com.github.savan.rickandmorty.repository.data.CharacterPage
import com.github.savan.rickandmorty.repository.data.Location

/**
 * Interface for Rick and Morty repository. Fetch data from Rick and Morty REST service and
 * cache responses.
 */
interface IRickAndMortyRepo {
    /**
     * Get location for given location id. Blocks for response.
     */
    suspend fun getLocation(locationId: Int): Location?

    /**
     * Get characters for given page number. Blocks for response.
     */
    suspend fun getCharactersForPage(page: Int): CharacterPage?
}