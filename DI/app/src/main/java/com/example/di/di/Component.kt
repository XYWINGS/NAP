package com.example.di.di

import com.example.di.data.Database
import com.example.di.data.ExampleRepositoryImpl
import com.example.di.domain.ExampleUseCase
import com.example.di.presentation.ExampleViewModel
import com.example.di.presentation.MainActivity

class Component {
    val database = Database()
    val repository = ExampleRepositoryImpl(database)
    val exampleUseCase = ExampleUseCase(repository)
    val exampleViewModel = ExampleViewModel(exampleUseCase)

    fun inject(mainActivity: MainActivity){
        mainActivity.exampleViewModel = exampleViewModel
    }
}
