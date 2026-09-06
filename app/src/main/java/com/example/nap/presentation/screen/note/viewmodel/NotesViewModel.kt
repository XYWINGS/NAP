package com.example.nap.presentation.screen.note.viewmodel

import androidx.lifecycle.ViewModel
import com.example.nap.data.repository.TestNotesRepositoryImpl
import com.example.nap.domain.model.Note
import com.example.nap.domain.usecase.AddNoteUseCase
import com.example.nap.domain.usecase.DeleteNoteUseCase
import com.example.nap.domain.usecase.EditNoteUseCase
import com.example.nap.domain.usecase.GetAllNotesUseCase
import com.example.nap.domain.usecase.GetNoteUseCase
import com.example.nap.domain.usecase.SearchNoteUseCase
import com.example.nap.domain.usecase.SwitchPinnedStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel : ViewModel() {
    //   FIXME
    /*   Using the repo here directly is a violation of the clean arch. The all asset of presentation layer
         should know nothing about the data layer. Use dependency injection instead
    */
    private val repository: TestNotesRepositoryImpl = TestNotesRepositoryImpl
    private val addNoteUseCase = AddNoteUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)

    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)

    private val searchNoteUseCase = SearchNoteUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)

    private val getAllNotesUseCommands = GetAllNotesUseCase(repository)

    //Parametrized mutable state flow variable to store all the user entered search queries
    private val query: MutableStateFlow<String> = MutableStateFlow("")

    //Screen state with backing property approach. A private mutable variable
    private val _state: MutableStateFlow<NoteScreenState> = MutableStateFlow(NoteScreenState())

    //Public property which subscribes from the view
    val state: StateFlow<NoteScreenState> = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    /*
        Init block will perform automatically as soon as the view is created. For example if the search query is
        empty will fetch the all the notes from the use case
    */
    init {
        addSomeNotes()
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
        }.launchIn(scope)
    }

    //The function that can be called from the view in order to handle all the commands
    fun processCommand(command: NotesCommands) {
        when (command) {
            is NotesCommands.DeleteNote -> {
                deleteNoteUseCase(command.noteId)
            }

            is NotesCommands.EditNote -> {
                val note = getNoteUseCase(command.note.id)
                val newTitle = note.title
                editNoteUseCase(note.copy(title = newTitle + "edited"))

            }
//            Everytime the search query changes the flow will react
            is NotesCommands.InputSearchQuery -> {
                query.update { command.query.trim() }
            }

            is NotesCommands.SwitchPinStatus -> {
                switchPinnedStatusUseCase(command.noteId)
            }
        }
    }

    private fun addSomeNotes() {
        repeat(100) {
            addNoteUseCase(title = "Title N$it", content = "content N$it")
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

    //TEMP commands for testing
    data class DeleteNote(val noteId: Int) : NotesCommands
    data class EditNote(val note: Note) : NotesCommands
}
