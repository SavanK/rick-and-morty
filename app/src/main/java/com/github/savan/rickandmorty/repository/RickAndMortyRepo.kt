package com.github.savan.rickandmorty.repository

import android.util.LruCache
import com.github.savan.rickandmorty.repository.data.CharacterPage
import com.github.savan.rickandmorty.repository.data.Location
import com.github.savan.rickandmorty.repository.remote.IServiceFactory
import com.github.savan.rickandmorty.repository.remote.RickAndMortyWebService
import retrofit2.await
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class RickAndMortyRepo(private val remoteServiceFactory: IServiceFactory): IRickAndMortyRepo {
    companion object {
        private const val TAG = "RickAndMortyRepo"

        private const val BASE_URL = "https://rickandmortyapi.com/api/"
        private const val LOCATION_CACHE_SIZE = 100
        private const val CHARACTER_PAGE_CACHE_SIZE = 20
    }

    private val locationsLock = ReentrantLock()
    private val locationsCache: LruCache<Int, Location> = LruCache(LOCATION_CACHE_SIZE)

    private val characterPagesLock = ReentrantLock()
    private val characterPagesCache: LruCache<CharacterPageKey, CharacterPage> = LruCache(
        CHARACTER_PAGE_CACHE_SIZE)

    private data class CharacterPageKey(val characterName: String, val page: Int)

    override suspend fun getLocation(locationId: Int): Location? {
        // cache hit
        locationsLock.withLock {
            locationsCache[locationId]?.let {
                return it
            }
        }

        // cache miss
        val rickAndMortyService = remoteServiceFactory.getRemoteService(BASE_URL,
            RickAndMortyWebService::class.java) as RickAndMortyWebService
        val location = rickAndMortyService.getLocation(locationId).await()
        locationsLock.withLock {
            // add to cache
            locationsCache.put(locationId, location)
        }

        return location
    }

    override suspend fun getCharactersForPage(page: Int, name: String): CharacterPage? {
        val characterPageKey = CharacterPageKey(name, page)

        // cache hit
        characterPagesLock.withLock {
            characterPagesCache[characterPageKey]?.let {
                return it
            }
        }

        // cache miss
        val rickAndMortyService = remoteServiceFactory.getRemoteService(BASE_URL,
            RickAndMortyWebService::class.java) as RickAndMortyWebService
        val characterPage = rickAndMortyService.getCharactersForPage(page, name).await()
        characterPagesLock.withLock {
            // add to cache
            characterPagesCache.put(characterPageKey, characterPage)
        }

        return characterPage
    }
}