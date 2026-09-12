package com.example.nap.data.repository

import android.content.Context
import com.example.nap.data.database.NotesDatabase
import com.example.nap.data.mapper.toEntities
import com.example.nap.data.mapper.toEntity
import com.example.nap.data.mapper.toNoteDBModel
import com.example.nap.data.model.NoteDBModel
import com.example.nap.domain.model.Note
import com.example.nap.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl private constructor(context: Context) : NotesRepository {
    private val notesDatabase = NotesDatabase.getInstance(context)
    private val notesDao = notesDatabase.notesDao()

    override suspend fun addNote(
        title: String, content: String, isPinned: Boolean, updatedAt: Long
    ) {
        val noteDbModel = NoteDBModel(
            id = 0, title, content, updatedAt, isPinned

        )
        notesDao.addNote(noteDbModel)
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)

    }

    override suspend fun editNote(note: Note) {
        notesDao.addNote(note.toNoteDBModel())
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map { it.toEntities() }
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toEntity()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map { it.toEntities() }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        return notesDao.switchPinnedStatus(noteId)
    }

    companion object {
        //        Implement double check singleton pattern
        private val LOCK = Any()
        private var instance: NotesRepositoryImpl? = null
        fun getInstance(context: Context): NotesRepositoryImpl {
//          Double check singleton implementation
//          First if the instance contain and object return it without syncing
            instance?.let { return it }
//          If no instance create one with synchronization block
            synchronized(LOCK) {
//              In case two threads come at the same time, both get null.
                instance?.let { return it }

                return NotesRepositoryImpl(context).also {
                    instance = it
                }
            }
        }
    }
}
