package com.example.di.data

import android.content.Context
import android.util.Log
import com.example.di.domain.Item

/*If you want to use a constructor argument as a variable declare it with a private variable*/

class Database private constructor(
    private val context: Context
) {
    fun exampleMethod(item: Item) {
        Log.d("exampleMethod", "Database example method $item $context")
    }

    companion object {
        private val LOCK = Any()
        private var instance: Database? = null

        fun getInstance(context: Context): Database {
            instance?.let { return it }

            synchronized(LOCK) {
                instance?.let { return it }

                return Database(context).also {
                    instance = it
                }
            }
        }
    }
}
