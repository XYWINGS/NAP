package com.example.nap.data.model

import androidx.room.Embedded
import androidx.room.Relation

/* Embedded tag will tell the room the instead o looking for a column named note model,take all of
*  its fields and use them to construct the NoteDbModel object */
data class NoteWithContentDbModel(
    @Embedded
    val noteDBModel: NoteDBModel,
    /*With the relation annotation we can tell the room how the tables are connected instead of writing join queries*/
    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val content: List<ContentItemDbModel>
)