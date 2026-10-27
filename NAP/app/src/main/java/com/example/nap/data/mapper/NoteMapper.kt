package com.example.nap.data.mapper

import com.example.nap.data.model.ContentItemDbModel
import com.example.nap.data.model.ContentType
import com.example.nap.data.model.NoteDBModel
import com.example.nap.data.model.NoteWithContentDbModel
import com.example.nap.domain.model.ContentItem
import com.example.nap.domain.model.Note

fun Note.toNoteDBModel(): NoteDBModel {
    return NoteDBModel(
        id, title, updatedAt, isPinned
    )
}

fun List<ContentItem>.toContentItemDbModels(noteId: Int): List<ContentItemDbModel> {
    return mapIndexed { index, contentItem ->
        when (contentItem) {
            is ContentItem.Image -> {
                ContentItemDbModel(
                    noteId = noteId,
                    contentType = ContentType.IMAGE,
                    content = contentItem.url,
                    order = index
                )
            }

            is ContentItem.Text -> {
                ContentItemDbModel(
                    noteId = noteId,
                    contentType = ContentType.TEXT,
                    content = contentItem.content,
                    order = index
                )
            }
        }
    }
}

fun List<ContentItemDbModel>.toContentItems(): List<ContentItem> {
    return map { contentItem ->
        when (contentItem.contentType) {
            ContentType.TEXT -> {
                ContentItem.Text(contentItem.content)
            }

            ContentType.IMAGE -> {
                ContentItem.Image(url = contentItem.content)
            }
        }

    }
}

fun NoteWithContentDbModel.toEntity(): Note {
    return Note(
        id = noteDBModel.id,
        title = noteDBModel.title,
        content = content.toContentItems(),
        updatedAt = noteDBModel.updatedAt,
        isPinned = noteDBModel.isPinned
    )
}

fun List<NoteWithContentDbModel>.toEntities(): List<Note> {
    return map { it.toEntity() }
}
