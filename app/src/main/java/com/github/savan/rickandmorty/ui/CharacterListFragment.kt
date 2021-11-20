package com.github.savan.rickandmorty.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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

        private const val SEARCH_TEXT_INPUT_DELAY = 200.toLong()
    }

    private lateinit var characterList: RecyclerView
    private lateinit var characterAdapter: CharacterListAdapter
    private lateinit var characterSearchName: EditText
    private var rickAndMortyViewModel: RickAndMortyViewModel? = null

    private lateinit var handler: UiHandler

    private class UiHandler(looper: Looper, private val fragment: CharacterListFragment): Handler(looper) {
        companion object {
            const val MSG_SEARCH_CHARACTER = 209
        }

        override fun handleMessage(msg: Message) {
            when(msg.what) {
                MSG_SEARCH_CHARACTER -> {
                    fragment.searchCharacters(msg.obj as String)
                }
            }
        }
    }

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
        characterSearchName = view.findViewById(R.id.character_name_search)

        val rickAndMortyApplication = requireActivity().application as RickAndMortyApplication
        rickAndMortyViewModel = ViewModelProvider(requireActivity(),
            rickAndMortyApplication.getViewModelFactory()).get(RickAndMortyViewModel::class.java)

        characterAdapter = CharacterListAdapter(rickAndMortyViewModel, CharacterComparator())
        characterList.layoutManager = LinearLayoutManager(view.context)
        characterList.adapter = characterAdapter

        handler = UiHandler(Looper.getMainLooper(), this)

        characterSearchName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                // fetch searched characters
                handler.removeMessages(UiHandler.MSG_SEARCH_CHARACTER)
                handler.sendMessageDelayed(
                    Message.obtain(handler, UiHandler.MSG_SEARCH_CHARACTER, s.toString()),
                    SEARCH_TEXT_INPUT_DELAY)
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            rickAndMortyViewModel?.getCharacterList()?.observe(viewLifecycleOwner, {
                // Received characters page. Update recycler view
                characterAdapter.submitData(lifecycle, it)
            })
        }
    }

    fun searchCharacters(name: String) {
        rickAndMortyViewModel?.setQueryCharacterName(name)
        characterAdapter.refresh()
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