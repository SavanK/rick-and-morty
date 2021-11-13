package com.github.savan.rickandmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.github.savan.rickandmorty.repository.RickAndMortyRepo
import com.github.savan.rickandmorty.ui.CharacterDetailFragment
import com.github.savan.rickandmorty.ui.CharacterListFragment
import com.github.savan.rickandmorty.viewmodel.RickAndMortyViewModel
import com.github.savan.rickandmorty.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var fragmentContainerView: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentContainerView = findViewById(R.id.fragment_container_view)

        val rickAndMortyApplication = application as RickAndMortyApplication
        val rickAndMortyViewModel = ViewModelProvider(this,
            rickAndMortyApplication.getViewModelFactory()).get(RickAndMortyViewModel::class.java)
        rickAndMortyViewModel.getSelectedCharacter().observe(this, {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragment_container_view, CharacterDetailFragment(it))
                addToBackStack("character_detail")
                commit()
            }
        })

        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container_view, CharacterListFragment())
            commit()
        }
    }
}