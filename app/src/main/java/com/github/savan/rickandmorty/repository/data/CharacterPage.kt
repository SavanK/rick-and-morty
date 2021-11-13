package com.github.savan.rickandmorty.repository.data

data class CharacterPage(val info: Info, val results: List<Character>) {
    data class Info(val count: Int, val pages: Int, val next: String, val prev: String)
}