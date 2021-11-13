package com.github.savan.rickandmorty.repository.data

data class Character(val id: Int, val name: String, val status: String, val species: String,
                     val location: LocationShort, val image: String) {
    data class LocationShort(val name: String, val url: String)
}
