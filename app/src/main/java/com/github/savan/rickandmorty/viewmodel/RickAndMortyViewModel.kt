package com.github.savan.rickandmorty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.github.savan.rickandmorty.repository.IRickAndMortyRepo
import com.github.savan.rickandmorty.repository.data.Character
import com.github.savan.rickandmorty.repository.data.CharacterPage
import com.github.savan.rickandmorty.repository.data.Location
import kotlinx.coroutines.launch
import java.lang.Exception

class RickAndMortyViewModel(private val rickAndMortyRepo: IRickAndMortyRepo): ViewModel() {
    companion object {
        private const val TAG = "RickAndMortyViewModel"
    }

    private val selectedCharacter: MutableLiveData<Character> = MutableLiveData()

    fun getSelectedCharacter(): LiveData<Character> {
        return selectedCharacter
    }

    fun setSelectedCharacter(character: Character) {
        selectedCharacter.value = character
    }

    fun getCharacterList(): LiveData<PagingData<Character>> {
        return Pager(PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                CharacterPagingSource(rickAndMortyRepo)
            }).liveData
    }

    fun getLocation(locationId: Int): LiveData<Location> {
        val location: MutableLiveData<Location> = MutableLiveData()
        viewModelScope.launch {
            val l = rickAndMortyRepo.getLocation(locationId)
            location.postValue(l)
        }
        return location
    }

    class CharacterPagingSource(private val rickAndMortyRepo: IRickAndMortyRepo):
        PagingSource<Int, Character>() {
        override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
            return try {
                val nextPage = params.key ?: 1
                val characterPage = rickAndMortyRepo.getCharactersForPage(nextPage)
                characterPage?.let {
                    LoadResult.Page(
                        data = it.results,
                        prevKey = null,
                        nextKey = getNextPage(it)
                    )
                } ?: LoadResult.Error(Exception("Page not found"))
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        private fun getNextPage(characterPage: CharacterPage): Int {
            val nextPageUrl = characterPage.info.next
            val parts = nextPageUrl.split("=")
            return parts[1].toInt()
        }
    }

}