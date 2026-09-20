package com.example.nap.data.mapper

import com.example.nap.data.model.ContentItemDbModel
import com.example.nap.data.model.NoteDBModel
import com.example.nap.domain.model.ContentItem
import com.example.nap.domain.model.Note
import kotlinx.serialization.json.Json

fun Note.toNoteDBModel(): NoteDBModel {
    val contentAsString = Json.encodeToString(content.toContentItemDbModels())
    return NoteDBModel(
        id, title, contentAsString, updatedAt, isPinned
    )
}

fun List<ContentItem>.toContentItemDbModels(): List<ContentItemDbModel> {
    return map { contentItem ->
        when (contentItem) {
            is ContentItem.Image -> {
                ContentItemDbModel.Image(url = contentItem.url)
            }

            is ContentItem.Text -> {
                ContentItemDbModel.Text(content = contentItem.content)

            }
        }
    }
}

fun List<ContentItemDbModel>.toContentItems(): List<ContentItem> {
    return map { contentItem ->
        when (contentItem) {
            is ContentItemDbModel.Image -> {
                ContentItem.Image(url = contentItem.url)
            }

            is ContentItemDbModel.Text -> {
                ContentItem.Text(content = contentItem.content)
            }
        }
    }
}

fun NoteDBModel.toEntity(): Note {
    val contents = Json.decodeFromString<List<ContentItemDbModel>>(content)
    return Note(
        id,
        title,
        contents.toContentItems(),
        updatedAt,
        isPinned
    )
}

fun List<NoteDBModel>.toEntities(): List<Note> {
    return map { it.toEntity() }
}
