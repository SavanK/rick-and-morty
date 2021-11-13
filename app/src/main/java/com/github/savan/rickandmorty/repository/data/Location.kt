package com.github.savan.rickandmorty.repository.data

/**
 * Data object representing the JSON response for a location
 */
data class Location(val id: Int, val name: String, val type: String, val dimension: String,
                    val residents: List<String>)
