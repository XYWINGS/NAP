package com.example.nap.domain.repository

import com.example.nap.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun addNote(title: String, content: String, isPinned: Boolean, updatedAt: Long)
    fun deleteNote(noteId: Int)
    fun editNote(note: Note)
    fun getAllNotes(): Flow<List<Note>>
    fun getNote(noteId: Int): Note
    fun searchNotes(query: String): Flow<List<Note>>
    fun switchPinnedStatus(noteId: Int)
}
