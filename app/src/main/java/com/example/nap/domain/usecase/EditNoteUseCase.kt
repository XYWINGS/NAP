package com.example.nap.domain.usecase

import com.example.nap.domain.repository.NotesRepository
import com.example.nap.domain.model.Note

class EditNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(note: Note) {
        repository.editNote(note)
    }
}
