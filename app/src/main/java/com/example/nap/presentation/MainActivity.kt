package com.example.nap.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nap.presentation.screen.note_creation.composable.CreateNoteScreen
import com.example.nap.presentation.ui.theme.NAPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NAPTheme {
//                NoteScreen(
//                    onNoteClick = {
//                        Log.d("MainActivity", "onNoteClick invoked $it")
//                    },
//                    onFloatingActionButtonClick = {
//                        Log.d("MainActivity", "onFloatingActionButtonClick invoked")
//                    },
//                )
                CreateNoteScreen(
                    onFinishNoteCreation = {
                        Log.d("MainActivity", "A new note created")
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "Activity destroyed")
    }
}
