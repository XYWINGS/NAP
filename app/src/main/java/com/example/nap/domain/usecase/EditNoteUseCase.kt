package com.example.nap.domain.usecase

import com.example.nap.domain.repository.NotesRepository
import com.example.nap.domain.model.Note

class EditNoteUseCase(
    private val repository: NotesRepository
) {
    /*The date time is not a client requirement and the user should not provide the data. The date is
    just a technical property needed to organize the data storage. So the data update logic falls to the
    domain layer
   */
    suspend operator fun invoke(note: Note) {
        repository.editNote(
            note.copy(
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}
