package com.github.savan.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.savan.rickandmorty.repository.IRickAndMortyRepo

class ViewModelFactory(private val rickAndMortyRepo: IRickAndMortyRepo):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when(modelClass) {
            RickAndMortyViewModel::class.java -> {
                return RickAndMortyViewModel(rickAndMortyRepo) as T
            }
        }
        return super.create(modelClass)
    }
}