package com.example.nap.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.nap.data.mapper.toContentItemDbModels
import com.example.nap.data.model.ContentItemDbModel
import com.example.nap.data.model.NoteDBModel
import com.example.nap.data.model.NoteWithContentDbModel
import com.example.nap.domain.model.ContentItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    /*If we marked the return data type with flow, the room will notify us whenever the db changes. All
    * subscribers will receive the new data */
    @Transaction
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteWithContentDbModel>>

    /*In here the || means concatenation in SQL*/
    @Query(
        """
        SELECT DISTINCT notes.* 
        FROM notes 
        JOIN content ON notes.id = content.noteId 
        WHERE title LIKE '%' || :query || '%' 
        OR content LIKE '%' || :query || '%' 
        ORDER BY updatedAt 
        DESC"""
    )
    fun searchNotes(query: String): Flow<List<NoteWithContentDbModel>>

    @Transaction
    @Query("DELETE FROM notes WHERE id == :noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("UPDATE notes SET isPinned = NOT isPinned WHERE id == :noteId")
    suspend fun switchPinnedStatus(noteId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteDBModel: NoteDBModel): Long

    @Query("SELECT * FROM notes WHERE id == :noteId")
    suspend fun getNote(noteId: Int): NoteWithContentDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNoteContent(content: List<ContentItemDbModel>)

    @Query("DELETE FROM content WHERE noteId == :noteId")
    suspend fun deleteNoteContent(noteId: Int)

    @Transaction
    suspend fun addNoteWithContent(
        noteDbModel: NoteDBModel,
        content: List<ContentItem>
    ) {
        val noteId = addNote(noteDbModel).toInt()
        val contentItems = content.toContentItemDbModels(noteId)
        addNoteContent(contentItems)
    }

    @Transaction
    suspend fun updateNoteWithContent(
        noteDbModel: NoteDBModel,
        content: List<ContentItemDbModel>
    ) {
        addNote(noteDbModel)
        deleteNoteContent(noteDbModel.id)
        addNoteContent(content)
    }


}
