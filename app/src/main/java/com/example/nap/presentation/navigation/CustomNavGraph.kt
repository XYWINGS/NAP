package com.example.nap.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nap.presentation.screen.note_creation.composable.CreateNoteScreen
import com.example.nap.presentation.screen.note_display.composable.NoteScreen
import com.example.nap.presentation.screen.note_edit.composable.EditNoteScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.NotesDisplayScreen.route
    ) {

        composable(Screen.NotesDisplayScreen.route) {
            NoteScreen(
                onNoteClick = {
                    navController.navigate(Screen.EditNoteScreen.route)
                },
                onFloatingActionButtonClick = {
                    navController.navigate(Screen.CreateNoteScreen.route)
                },
            )
        }
        composable(Screen.CreateNoteScreen.route) {
            CreateNoteScreen(
                onFinishNoteCreation = {
                    navController.popBackStack()
                })
        }
        composable(Screen.EditNoteScreen.route) {
            EditNoteScreen(
                onFinishNoteCreation = {
                    navController.popBackStack()
                },
                noteId = 5,
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object NotesDisplayScreen : Screen("NotesDisplayScreen")
    data object CreateNoteScreen : Screen("CreateNoteScreen")
    data object EditNoteScreen : Screen("EditNoteScreen")
}
