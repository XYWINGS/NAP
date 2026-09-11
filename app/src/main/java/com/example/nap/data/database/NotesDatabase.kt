package com.example.nap.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nap.data.dao.NotesDao
import com.example.nap.data.model.NoteDBModel

/*If the table structure updates change the version number as well. Otherwise, the app will crash*/
@Database(entities = [NoteDBModel::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        //       A nullable mutable variable that holds an instance of the notes database
        private var instance: NotesDatabase? = null

        //      The singleton must be thread safe, so we use a synchronization block. This object will server as a lock
        private val LOCK = Any()

        //      When we need the db we call the get instance and pass the context
        fun getInstance(context: Context): NotesDatabase {
//          Double check singleton implementation
//          First if the instance contain and object return it without syncing
            instance?.let { return it }
//          If no instance create one with synchronization block
            synchronized(LOCK) {
//              In case two threads come at the same time, both get null.
                instance?.let { return it }

                return Room.databaseBuilder(
                    context = context, klass = NotesDatabase::class.java, name = "notes.db"
                ).build().also {
                    instance = it
                }
            }
        }
    }
}
