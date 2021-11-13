package com.github.savan.rickandmorty.repository.remote

/**
 * Interface for service factory that creates and holds remote services
 */
interface IServiceFactory {

    /**
     * Returns a remote service end point for the base URL and service class object
     */
    fun getRemoteService(baseUrl: String, klass: Class<*>): Any
}