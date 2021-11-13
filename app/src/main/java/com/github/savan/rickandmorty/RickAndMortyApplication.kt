package com.github.savan.rickandmorty

import android.app.Application
import com.github.savan.rickandmorty.repository.IRickAndMortyRepo
import com.github.savan.rickandmorty.repository.RickAndMortyRepo
import com.github.savan.rickandmorty.repository.remote.ServiceFactory
import com.github.savan.rickandmorty.viewmodel.ViewModelFactory

class RickAndMortyApplication: Application() {
    private lateinit var rickAndMortyRepo: IRickAndMortyRepo
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate() {
        super.onCreate()

        // Rick and Morty repository is tied to application lifecycle. This ensures the data cached
        // in the repository lives on even after the Activity's ViewModel is destroyed.
        rickAndMortyRepo = RickAndMortyRepo(ServiceFactory())
        viewModelFactory = ViewModelFactory(rickAndMortyRepo)
    }

    fun getViewModelFactory(): ViewModelFactory {
        return viewModelFactory
    }
}