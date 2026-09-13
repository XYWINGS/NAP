package com.example.nap.domain.use_case

import com.example.nap.domain.repository.NotesRepository
import javax.inject.Inject

class SwitchPinnedStatusUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(noteId: Int) {
        repository.switchPinnedStatus(noteId)
    }
}
