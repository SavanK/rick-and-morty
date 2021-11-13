package com.github.savan.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.savan.rickandmorty.R
import com.github.savan.rickandmorty.RickAndMortyApplication
import com.github.savan.rickandmorty.repository.data.Character
import com.github.savan.rickandmorty.viewmodel.RickAndMortyViewModel
import kotlinx.coroutines.launch

class CharacterListFragment: Fragment() {
    companion object {
        private const val TAG = "CharacterListFragment"
    }

    private lateinit var characterList: RecyclerView
    private var rickAndMortyViewModel: RickAndMortyViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterList = view.findViewById(R.id.character_list)

        val rickAndMortyApplication = requireActivity().application as RickAndMortyApplication
        rickAndMortyViewModel = ViewModelProvider(requireActivity(),
            rickAndMortyApplication.getViewModelFactory()).get(RickAndMortyViewModel::class.java)

        val characterAdapter = CharacterListAdapter(rickAndMortyViewModel, CharacterComparator())
        characterList.layoutManager = LinearLayoutManager(view.context)
        characterList.adapter = characterAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            rickAndMortyViewModel?.getCharacterList()?.observe(viewLifecycleOwner, {
                characterAdapter.submitData(lifecycle, it)
            })
        }
    }

    private class CharacterListAdapter(private val rickAndMortyViewModel: RickAndMortyViewModel?,
                                       characterComparator: CharacterComparator):
        PagingDataAdapter<Character, CharacterListAdapter.CharacterViewHolder>(characterComparator) {

        inner class CharacterViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
            init {
                view.setOnClickListener {
                    val character = getItem(bindingAdapterPosition)
                    character?.let {
                        rickAndMortyViewModel?.setSelectedCharacter(character)
                    }
                }
            }

            fun bind(character: Character?) {
                val characterName: TextView = view.findViewById(R.id.character_name)
                val characterSpecies: TextView = view.findViewById(R.id.character_species)
                val characterStatus: TextView = view.findViewById(R.id.character_status)

                characterName.text = character?.name?: ""
                characterSpecies.text = character?.species?: ""
                characterStatus.text = character?.status?: ""
            }
        }

        override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
            val character = getItem(position)
            holder.bind(character)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.character_list_item, parent, false)
            return CharacterViewHolder(view)
        }
    }

    private class CharacterComparator: DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }

}