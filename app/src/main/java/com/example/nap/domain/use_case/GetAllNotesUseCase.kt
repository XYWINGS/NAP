package com.example.nap.domain.use_case

import com.example.nap.domain.repository.NotesRepository
import com.example.nap.domain.model.Note
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(): Flow<List<Note>> {
       return repository.getAllNotes()
    }
}
