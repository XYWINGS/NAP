package com.example.di

import android.app.Application
import com.example.di.di.Component
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExampleApp : Application() {
    //For manual DI
//    val component = Component(this)
}

/*At compile time there is no context, for it to be created and added to the app,
the application must be launched. That is why in a class that represents the entier application a
component is created and the context is added to it */
