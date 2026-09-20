package com.example.nap.di

import android.content.Context
import androidx.room.Room
import com.example.nap.data.dao.NotesDao
import com.example.nap.data.database.NotesDatabase
import com.example.nap.data.repository.NotesRepositoryImpl
import com.example.nap.domain.repository.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    /*  With DI you can easily switch between test and real implementations*/
//  Test Impl
//    @Singleton
//    @Binds
//    fun bindNotesRepository(
//        impl: TestNotesRepositoryImpl
//    ): NotesRepository

    //    Real Impl
    @Singleton
    @Binds
    fun bindNotesRepository(
        impl: NotesRepositoryImpl
    ): NotesRepository

    companion object {

        @Singleton
        @Provides
        fun providedDatabase(
            @ApplicationContext context: Context
        ): NotesDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = NotesDatabase::class.java,
                name = "notes.db"
            ).fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }

        @Singleton
        @Provides
        fun providedNotesDao(
            database: NotesDatabase
        ): NotesDao {
            return database.notesDao()
        }

    }

}
