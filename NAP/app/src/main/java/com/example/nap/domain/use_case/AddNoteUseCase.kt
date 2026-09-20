package com.example.nap.domain.use_case

import com.example.nap.domain.model.ContentItem
import com.example.nap.domain.repository.NotesRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(
        title: String, content: List<ContentItem>
    ) {/*
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
