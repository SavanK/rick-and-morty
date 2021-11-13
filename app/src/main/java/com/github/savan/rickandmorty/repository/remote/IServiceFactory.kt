package com.github.savan.rickandmorty.repository.remote

interface IServiceFactory {
    fun getRemoteService(baseUrl: String, klass: Class<*>): Any
}