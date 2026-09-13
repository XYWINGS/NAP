package com.example.di

import android.app.Application
import com.example.di.di.Component

class ExampleApp : Application() {
    val component = Component(this)
}
