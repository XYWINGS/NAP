package com.example.nap.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nap.presentation.navigation.NavGraph
import com.example.nap.presentation.ui.theme.NAPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NAPTheme {
                NavGraph()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "Activity destroyed")
    }
}

/*Context is a abstract class used to interact with the OS.
If you are inside an activity, not inside a composable function, then we can use the 'this' as context
* because it directly inherits from it. Remember the application context and activity context are different things.
* If a screen rotates the activity context will be destroyed but app context will exist, Keep this in mind when
* working with databases since the db should be singleton and there shouldn't be multiple instances
* of a db running through the app. */
