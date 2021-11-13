package com.github.savan.rickandmorty.repository.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ServiceFactory: IServiceFactory {
    private val remoteServices = mutableMapOf<Pair<String, Class<*>>, Any>()

    private fun createService(baseUrl: String, klass: Class<*>): Any {
        val gson = GsonBuilder().setLenient().create()
        val serviceEndpoint = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        return serviceEndpoint.create(klass)
    }

    override fun getRemoteService(baseUrl: String, klass: Class<*>) : Any {
        val serviceKey = Pair(baseUrl, klass)
        if (!remoteServices.containsKey(serviceKey)) {
            remoteServices[serviceKey] = createService(baseUrl, klass)
        }
        return remoteServices[serviceKey]!!
    }
}