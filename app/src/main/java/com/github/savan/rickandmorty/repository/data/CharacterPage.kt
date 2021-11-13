package com.github.savan.rickandmorty.repository.data

/**
 * Data object representing the JSON response for a page of characters
 */
data class CharacterPage(val info: Info, val results: List<Character>) {
    data class Info(val count: Int, val pages: Int, val next: String, val prev: String)
}