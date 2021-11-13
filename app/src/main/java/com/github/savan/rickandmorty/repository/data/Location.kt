package com.github.savan.rickandmorty.repository.data

data class Location(val id: Int, val name: String, val type: String, val dimension: String,
                    val residents: List<String>)
