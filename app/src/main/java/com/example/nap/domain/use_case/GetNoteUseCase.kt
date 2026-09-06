package com.example.nap.domain.use_case

import com.example.nap.domain.repository.NotesRepository
import com.example.nap.domain.model.Note

class GetNoteUseCase(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(noteId: Int): Note {
        return repository.getNote(noteId)
    }
}
