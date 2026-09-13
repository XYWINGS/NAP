package com.example.di.di

import android.content.Context
import com.example.di.data.Database
import com.example.di.data.ExampleRepositoryImpl
import com.example.di.domain.ExampleRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*This will provide implementation of classes from the data layer*/

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindExampleRepository(
        impl: ExampleRepositoryImpl
    ): ExampleRepository

    companion object {
        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context
        ): Database {
            return Database.getInstance(context)
        }
    }
}
