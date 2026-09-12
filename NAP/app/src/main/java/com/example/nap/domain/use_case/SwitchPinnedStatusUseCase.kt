package com.example.nap.domain.use_case

import com.example.nap.domain.repository.NotesRepository

class SwitchPinnedStatusUseCase(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(noteId: Int) {
        repository.switchPinnedStatus(noteId)
    }
}
