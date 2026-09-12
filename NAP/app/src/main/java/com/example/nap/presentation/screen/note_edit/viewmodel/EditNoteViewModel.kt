package com.example.nap.presentation.screen.note_edit.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nap.data.repository.NotesRepositoryImpl
import com.example.nap.domain.model.Note
import com.example.nap.domain.use_case.DeleteNoteUseCase
import com.example.nap.domain.use_case.EditNoteUseCase
import com.example.nap.domain.use_case.GetNoteUseCase
import com.example.nap.presentation.screen.note_edit.viewmodel.EditNoteState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditNoteViewModel(private val noteId: Int, context: Context) : ViewModel() {
    private val repository = NotesRepositoryImpl.getInstance(context)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val deleteNoteState = DeleteNoteUseCase(repository)
    private val _state = MutableStateFlow<EditNoteState>(Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                val note = getNoteUseCase(noteId)
                Editing(note)
            }
        }
    }

    fun processCommand(command: EditNoteCommand) {
        when (command) {
            EditNoteCommand.Back -> {
                _state.update { Finished }
            }

            is EditNoteCommand.InputContent -> {
                _state.update { previousState: EditNoteState ->
                    if (previousState is Editing) {
                        val newNote = previousState.note.copy(content = command.content)
                        previousState.copy(note = newNote)
                    } else {
                        previousState
                    }
                }
            }

            is EditNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if (previousState is Editing) {
                        val newNote = previousState.note.copy(title = command.title)
                        previousState.copy(note = newNote)
                    } else {
                        previousState
                    }
                }

            }/*When saving if we are in the saving state save the new node, if not keep the previous state and return it*/
            EditNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is Editing) {
                            val note = previousState.note
                            editNoteUseCase(note)
                            Finished
                        } else {
                            previousState
                        }
                    }
                }
            }

            EditNoteCommand.Delete -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is Editing) {
                            val note = previousState.note
                            deleteNoteState(note.id)
                            Finished
                        } else {
                            previousState
                        }
                    }
                }
            }
        }
    }
}

sealed interface EditNoteCommand {
    data class InputTitle(val title: String) : EditNoteCommand
    data class InputContent(val content: String) : EditNoteCommand
    data object Save : EditNoteCommand
    data object Back : EditNoteCommand
    data object Delete : EditNoteCommand
}

sealed interface EditNoteState {
    data object Initial : EditNoteState
    data class Editing(
        val note: Note
    ) : EditNoteState {
        val isSaveEnabled: Boolean
            get() = note.title.isNotBlank() && note.content.isNotBlank()
    }

    data object Finished : EditNoteState
}
