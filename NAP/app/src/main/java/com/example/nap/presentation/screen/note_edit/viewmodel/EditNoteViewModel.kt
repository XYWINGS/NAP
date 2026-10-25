package com.example.nap.presentation.screen.note_edit.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nap.domain.model.ContentItem
import com.example.nap.domain.model.Note
import com.example.nap.domain.use_case.DeleteNoteUseCase
import com.example.nap.domain.use_case.EditNoteUseCase
import com.example.nap.domain.use_case.GetNoteUseCase
import com.example.nap.presentation.screen.note_edit.viewmodel.EditNoteState.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditNoteViewModel.Factory::class)
class EditNoteViewModel @AssistedInject constructor(
    private val editNoteUseCase: EditNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val deleteNoteState: DeleteNoteUseCase,
    @Assisted("noteId") private val noteId: Int
) : ViewModel() {
    private val _state = MutableStateFlow<EditNoteState>(Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                val note = getNoteUseCase(noteId)
                /*If the last content item is an image,In order to add some text after it
                * we add this check here. If it's an image, add an empty edittext after it */
                val content = if (note.content.lastOrNull() !is ContentItem.Text) {
                    note.content + ContentItem.Text("")
                } else {
                    note.content
                }
                Editing(note.copy(content = content))
            }
        }
    }

    fun processCommand(command: EditNoteCommand) {
        when (command) {
            EditNoteCommand.Back -> {
                _state.update { Finished }
            }

            is EditNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if (previousState is Editing) {
                        val newContent = previousState.note.content
                            .mapIndexed { index, item ->
                                if (index == command.index && item is ContentItem.Text) {
                                    item.copy(content = command.content)
                                } else {
                                    item
                                }
                            }
                        val newNote = previousState.note.copy(content = newContent)
                        previousState.copy(
                            note = newNote,
                        )

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
            is EditNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is Editing) {
                            val note = previousState.note
                            val content = previousState.note.content.filter {
                                it !is ContentItem.Text || it.content.isNotBlank()
                            }
                            editNoteUseCase(note.copy(content = content))
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

            is EditNoteCommand.AddImage -> {
                _state.update { previousState ->
                    if (previousState is Editing) {
                        val oldNote = previousState.note
                        oldNote.content.toMutableList().apply {
                            val lastItem = last()
                            if (lastItem is ContentItem.Text && lastItem.content.isBlank()) {
                                removeAt(lastIndex)
                            }
                            add(ContentItem.Image(command.uri.toString()))
                            add(ContentItem.Text(""))
                        }.let {
                            val newNote = oldNote.copy(content = it)
                            previousState.copy(note = newNote)
                        }
                    } else {
                        previousState
                    }
                }
            }

            is EditNoteCommand.DeleteImage -> {
                _state.update { previousState ->
                    if (previousState is Editing) {
                        val oldNote = previousState.note

                        oldNote.content.toMutableList().apply {
                            removeAt(command.index)
                        }.let {
                            val newNote = oldNote.copy(content = it)
                            previousState.copy(note = newNote)
                        }
                    } else {
                        previousState
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("noteId") noteId: Int
        ): EditNoteViewModel
    }

}

sealed interface EditNoteCommand {
    data class InputTitle(val title: String) : EditNoteCommand
    data class InputContent(val content: String, val index: Int) : EditNoteCommand
    data object Save : EditNoteCommand
    data object Back : EditNoteCommand
    data object Delete : EditNoteCommand
    data class AddImage(val uri: Uri) : EditNoteCommand
    data class DeleteImage(val index: Int) : EditNoteCommand
}

sealed interface EditNoteState {
    data object Initial : EditNoteState
    data class Editing(
        val note: Note
    ) : EditNoteState {
        /*Following will check and enable the save button if the title or any of the content type is available*/
        val isSaveEnabled: Boolean
            get() {
                return when {
                    note.title.isBlank() -> false
                    note.content.isEmpty() -> false
                    else -> {
                        note.content.any {
                            it !is ContentItem.Text || it.content.isNotBlank()
                        }
                    }
                }
            }
    }

    data object Finished : EditNoteState
}
