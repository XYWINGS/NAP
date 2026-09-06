package com.example.nap.data.repository

import com.example.nap.domain.model.Note
import com.example.nap.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class NotesRepositoryImpl : NotesRepository {

    private val notesFlowList: MutableStateFlow<List<Note>> = MutableStateFlow(listOf())

    override fun addNote(note: Note) {/*Mod modify the state flow we make it mutable add the new data and then assign it to the state flow again*/

//        Method 1
//        val newNote: MutableList<Note> = notesFlowList.value.toMutableList()
//        newNote.add(note)
//        notesFlowList.value = newNote

//        Method 2
//        notesFlowList.update {
//            it.toMutableList().apply {
//                add(note)
//            }
//        }

//        Method 3
//        Here the + is overridden and works exactly same as above (see the doc)
        notesFlowList.update {
            it + note
        }

    }

    override fun deleteNote(noteId: Int) {
        notesFlowList.update {
            it.toMutableList().apply {
                removeIf { note ->
                    note.id == noteId
                }
            }
        }
    }

    override fun editNote(note: Note) {
        notesFlowList.update { oldList ->
            oldList.map { listNote ->
                if (listNote.id == note.id) {
                    note
                } else {
                    listNote
                }
            }
        }
    }

    override fun getAllNotes(): Flow<List<Note>> {
//        We add .asStateFlow() in order to stop, downcast the list and update the values futher down the stream.
//        This will make it immutable
        return notesFlowList.asStateFlow()
    }

    //    There is a possibility of the note wasn't been found. Use the firstOrNUll method and handle the null path, otherwise it may crash
    override fun getNote(noteId: Int): Note {
        return notesFlowList.value.first { it.id == noteId }
    }

    override fun switchPinnedStatus(noteId: Int) {
        notesFlowList.update { oldList ->
            oldList.map { listNote ->
                if (listNote.id == noteId) {
                    listNote.copy(isPinned = !listNote.isPinned)
                } else {
                    listNote
                }
            }
        }
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesFlowList.map { currentList ->
            currentList.filter {
                it.title.contains(query) || it.content.contains(query)
            }
        }
    }
}
