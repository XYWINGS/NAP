package com.example.di.data

import android.content.Context
import android.util.Log
import com.example.di.domain.Item

/*If you want to use a constructor argument as a variable declare it with a private variable*/
class Database(private val context: Context) {
    fun exampleMethod(item: Item) {
        Log.d("exampleMethod", "Database example method $item $context")
    }
}
