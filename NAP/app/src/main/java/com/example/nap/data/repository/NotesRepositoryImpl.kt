package com.example.nap.data.repository

import com.example.nap.data.dao.NotesDao
import com.example.nap.data.file_manager.ImageFileManager
import com.example.nap.data.mapper.toContentItemDbModels
import com.example.nap.data.mapper.toEntities
import com.example.nap.data.mapper.toEntity
import com.example.nap.data.mapper.toNoteDBModel
import com.example.nap.data.model.NoteDBModel
import com.example.nap.domain.model.ContentItem
import com.example.nap.domain.model.Note
import com.example.nap.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao,
    private val imageFileManager: ImageFileManager
) : NotesRepository {

    override suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        isPinned: Boolean,
        updatedAt: Long
    ) {
        val processedContent = content.processForStorage()
        val noteDbModel = NoteDBModel(
            id = 0,
            title,
            updatedAt,
            isPinned
        )
        notesDao.addNoteWithContent(noteDbModel, processedContent)
    }

    override suspend fun deleteNote(noteId: Int) {
        val note = notesDao.getNote(noteId).toEntity()
        note.content
            .filterIsInstance<ContentItem.Image>()
            .map { it.url }
            .forEach { imageFileManager.deleteImage(it) }

        notesDao.deleteNote(noteId)

    }

    /*When editing, we subtract the new content from the odl content which will give us a new collection
    * of removed content. Then we call delete method on them. Then process them and save them.*/
    override suspend fun editNote(note: Note) {

        val oldNote = notesDao.getNote(note.id).toEntity()
        val oldUrls = oldNote.content.filterIsInstance<ContentItem.Image>().map { it.url }
        val newUrls = note.content.filterIsInstance<ContentItem.Image>().map { it.url }

        val removedUrls = oldUrls - newUrls.toSet()
        removedUrls.forEach { imageFileManager.deleteImage(it) }

        val processedContent = note.content.processForStorage()
        val processedNote = note.copy(content = processedContent)

        notesDao.updateNoteWithContent(
            processedNote.toNoteDBModel(),
            processedContent.toContentItemDbModels(note.id)
        )

    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map { it.toEntities() }
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toEntity()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map { it.toEntities() }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        return notesDao.switchPinnedStatus(noteId)
    }

    private suspend fun List<ContentItem>.processForStorage(): List<ContentItem> {
        return map { contentItem ->
            when (contentItem) {
                is ContentItem.Image -> {
                    if (imageFileManager.isInternal(contentItem.url)) {
                        contentItem
                    } else {
                        val internalPath =
                            imageFileManager.copyImageToInternalStorage(contentItem.url)
                        ContentItem.Image(internalPath)
                    }
                }

                is ContentItem.Text -> {
                    contentItem
                }
            }
        }
    }
//    companion object {
//        //        Implement double check singleton pattern
//        private val LOCK = Any()
//        private var instance: NotesRepositoryImpl? = null
//        fun getInstance(context: Context): NotesRepositoryImpl {
////          Double check singleton implementation
////          First if the instance contain and object return it without syncing
//            instance?.let { return it }
////          If no instance create one with synchronization block
//            synchronized(LOCK) {
////              In case two threads come at the same time, both get null.
//                instance?.let { return it }
//
//                return NotesRepositoryImpl(context).also {
//                    instance = it
//                }
//            }
//        }
//    }
}
