package com.example.nap.data.repository

import com.example.nap.domain.model.Note
import com.example.nap.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/*
 Declaring the repo as an object will make it a singleton class. So there will be no data mismatches
 when used across multiple classes
*/

object TestNotesRepositoryImpl : NotesRepository {

    private val addSomeNotes: MutableList<Note> = mutableListOf<Note>().apply {
        repeat(100) {
            add(
                Note(
                    id = it,
                    title = "Title N$it",
                    content = "You usually access ViewModel instances at screen-level composables, that is, close to a root composable called from an activity, fragment, or destination of a Navigation graph. This is because ViewModels are, by default, scoped to those screen level objects. Read more about a ViewModel's lifecycle and scope here.\n" + "\n" + "Try to avoid passing down ViewModel instances to other composables as this can make those composables more difficult to test and can break previews. Instead, pass only the data and functions they need as parameters.\n" + "\n" + "You can use ViewModel instances to manage state for sub screen-level composables, however, be aware of the ViewModel's lifecycle and scope. If the composable is self-contained, you may want to consider using Hilt to inject the ViewModel to avoid having to pass dependencies from parent composables.\n" + "\n" + "If your ViewModel has dependencies, viewModel() takes an optional ViewModelProvider.Factory as a parameter.\n" + "\n" + "For more information about ViewModel in Compose and how instances are used with the Navigation Compose library, or activities and fragments, see the Interoperability docs.",
                    updatedAt = System.currentTimeMillis(),
                    isPinned = false
                )
            )
        }
    }

    private val notesFlowList = MutableStateFlow<List<Note>>(addSomeNotes)

    override suspend fun addNote(
        title: String, content: String, isPinned: Boolean, updatedAt: Long
    ) {/*Mod modify the state flow we make it mutable add the new
           data and then assign it to the state flow again*/

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
        /*
        If the repo implementations performs a long operation using suspend functions will not block the
        * main thread and the users can keep working on it
        */
        notesFlowList.update { oldList ->
            val note = Note(
                id = oldList.size,
                title = title,
                content = content,
                updatedAt = updatedAt,
                isPinned = isPinned
            )
            oldList + note
        }

    }

    override suspend fun deleteNote(noteId: Int) {
        notesFlowList.update {
            it.toMutableList().apply {
                removeIf { note ->
                    note.id == noteId
                }
            }
        }
    }

    override suspend fun editNote(note: Note) {
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

    //    There is a possibility of the note wasn't found. Use the firstOrNUll method and handle the null path, otherwise it may crash
    override suspend fun getNote(noteId: Int): Note {
        return notesFlowList.value.first { it.id == noteId }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
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
