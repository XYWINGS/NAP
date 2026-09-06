package com.example.nap.domain.usecase

import com.example.nap.domain.repository.NotesRepository
import com.example.nap.domain.model.Note

class AddNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(
        title: String, content: String
    ) {
        repository.addNote(title, content)
    }
}
