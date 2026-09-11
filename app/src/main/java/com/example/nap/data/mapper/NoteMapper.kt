package com.example.nap.data.mapper

import com.example.nap.data.model.NoteDBModel
import com.example.nap.domain.model.Note

fun Note.toNoteDBModel(): NoteDBModel {
    return NoteDBModel(
        id, title, content, updatedAt, isPinned
    )
}

fun NoteDBModel.toEntity(): Note {
    return Note(
        id, title, content, updatedAt, isPinned
    )
}

fun List<NoteDBModel>.toEntities(): List<Note> {
    return map { it.toEntity() }
}
