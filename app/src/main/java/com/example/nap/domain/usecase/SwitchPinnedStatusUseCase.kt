package com.example.nap.domain.usecase

import com.example.nap.domain.repository.NotesRepository
import com.example.nap.domain.model.Note

class SwitchPinnedStatusUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(noteId: Int) {
        repository.switchPinnedStatus(noteId)
    }
}
