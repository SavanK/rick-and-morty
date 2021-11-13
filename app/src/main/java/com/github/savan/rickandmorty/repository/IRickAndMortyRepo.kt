package com.github.savan.rickandmorty.repository

import com.github.savan.rickandmorty.repository.data.CharacterPage
import com.github.savan.rickandmorty.repository.data.Location

interface IRickAndMortyRepo {
    suspend fun getLocation(locationId: Int): Location?

    suspend fun getCharactersForPage(page: Int): CharacterPage?
}