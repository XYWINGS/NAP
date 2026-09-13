package com.example.di.data

import android.util.Log
import com.example.di.domain.ExampleRepository
import com.example.di.domain.Item
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val database: Database
) : ExampleRepository {
    override fun exampleMethod(item: Item) {
        Log.d("ExampleRepositoryImpl", "ExampleRepositoryImpl example method $item")
    }
}
