package com.github.savan.rickandmorty.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.savan.rickandmorty.R
import com.github.savan.rickandmorty.RickAndMortyApplication
import com.github.savan.rickandmorty.repository.data.Character
import com.github.savan.rickandmorty.viewmodel.RickAndMortyViewModel
import com.squareup.picasso.Picasso

class CharacterDetailFragment(private val character: Character): Fragment() {
    companion object {
        private const val TAG = "CharacterDetailFragment"
    }

    private var rickAndMortyViewModel: RickAndMortyViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character_detail, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val characterName: TextView = view.findViewById(R.id.character_name)
        val characterImage: ImageView = view.findViewById(R.id.character_image)
        characterName.text = character.name
        Picasso.with(requireActivity()).load(character.image).into(characterImage)

        val locationName: TextView = view.findViewById(R.id.location_name)
        val locationType: TextView = view.findViewById(R.id.location_type)
        val locationDimen: TextView = view.findViewById(R.id.location_dimen)
        val locationPopulation: TextView = view.findViewById(R.id.location_population)

        val rickAndMortyApplication = requireActivity().application as RickAndMortyApplication
        rickAndMortyViewModel = ViewModelProvider(requireActivity(),
            rickAndMortyApplication.getViewModelFactory()).get(RickAndMortyViewModel::class.java)

        rickAndMortyViewModel?.getLocation(getLocationId())?.observe(viewLifecycleOwner, {
            locationName.text = getString(R.string.location_name, it.name)
            locationType.text = getString(R.string.location_type, it.type)
            locationDimen.text = getString(R.string.location_dimen, it.dimension)
            locationPopulation.text = getString(R.string.location_residents, it.residents.size)
        })

        // Added to consume touch events when this fragment is visible,
        // or else it travels along backstack to the list fragment beneath
        view.setOnTouchListener { _, _ -> true }
    }

    private fun getLocationId(): Int {
        val parts = character.location.url.split("/")
        return parts[parts.size-1].toInt()
    }
}