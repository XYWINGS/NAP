package com.example.nap.presentation.screen.note_display.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nap.data.repository.NotesRepositoryImpl
import com.example.nap.domain.model.Note
import com.example.nap.domain.use_case.GetAllNotesUseCase
import com.example.nap.domain.use_case.SearchNoteUseCase
import com.example.nap.domain.use_case.SwitchPinnedStatusUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(context: Context) : ViewModel() {
    //   FIXME
    /*   Using the repo here directly is a violation of the clean arch. The all asset of presentation layer
         should know nothing about the data layer. Use dependency injection instead
    */
    private val repository: NotesRepositoryImpl = NotesRepositoryImpl.getInstance(context)
    private val searchNoteUseCase = SearchNoteUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)
    private val getAllNotesUseCommands = GetAllNotesUseCase(repository)

    //Parametrized mutable state flow variable to store all the user entered search queries
    private val query: MutableStateFlow<String> = MutableStateFlow("")

    //Screen state with backing property approach. A private mutable variable
    private val _state: MutableStateFlow<NoteScreenState> = MutableStateFlow(NoteScreenState())

    //Public property which subscribes from the view
    val state: StateFlow<NoteScreenState> = _state.asStateFlow()

    /*
     If you defined custom scope, clear them on the onClear function. Else use the vieModelScope
     private val scope = CoroutineScope(Dispatchers.IO)
     */

    /*
        Init block will perform automatically as soon as the view is created. For example if the search query is
        empty will fetch the all the notes from the use case
    */
    init {
//        addSomeNotes()
        query.onEach { input ->
            _state.update { it.copy(query = input) }
        }.flatMapLatest {
            if (it.isBlank()) {
                getAllNotesUseCommands()
            } else {
                searchNoteUseCase(it)
            }
        }.onEach { map ->
            val pinnedNotes = map.filter { it.isPinned }
            val otherNotes = map.filter { !it.isPinned }
            _state.update { it.copy(pinnedNotes = pinnedNotes, otherNotes = otherNotes) }
        }.launchIn(viewModelScope)
    }

    /*Even the main activity that host the viewmodel destroys, the viewmodel continuous to live. For example
    * the screen rotation does not destroy the view model*/
    @SuppressLint("EmptySuperCall")
    override fun onCleared() {
        super.onCleared()
        Log.d("NotesViewModel", "View Model Destroyed")
//        viewModelScope.cancel()
    }

    //The function that can be called from the view in order to handle all the commands
    /*
    coroutines must be launched within a scope with a defined lifecycle
    * */
    fun processCommand(command: NotesCommands) {
        viewModelScope.launch {
            when (command) {
//            Everytime the search query changes the flow will react
                is NotesCommands.InputSearchQuery -> {
                    query.update { command.query.trim() }
                }

                is NotesCommands.SwitchPinStatus -> {
                    switchPinnedStatusUseCase(command.noteId)
                }
            }
        }
    }
}

//By looking at the viewmodel you should be able to tell what the user is currently seeing on the screen
data class NoteScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf()
)

// An interface with all the actions the user can perform in the related view as commands
sealed interface NotesCommands {
    data class InputSearchQuery(val query: String) : NotesCommands
    data class SwitchPinStatus(val noteId: Int) : NotesCommands
}
