package com.example.nap.domain.usecase

import com.example.nap.domain.repository.NotesRepository

class AddNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(
        title: String, content: String
    ) {
        /*
        If the client required to make the pin state as false as default, then that responsibility falls in to the usecase
        If it's about the data as it is stored, then it belongs to the data layer.
        If it's about interacting with the user, it belongs to the presentation layer
        */
        repository.addNote(
            title = title,
            content = content,
            isPinned = false,
            updatedAt = System.currentTimeMillis()
        )
    }
}
