package com.example.nap.domain.usecase

import com.example.nap.domain.repository.NotesRepository

class DeleteNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}
