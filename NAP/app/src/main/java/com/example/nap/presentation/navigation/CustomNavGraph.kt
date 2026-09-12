package com.example.nap.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nap.presentation.screen.note_creation.composable.CreateNoteScreen
import com.example.nap.presentation.screen.note_display.composable.NoteScreen
import com.example.nap.presentation.screen.note_edit.composable.EditNoteScreen

/*When we click on a list item, the createRoot() method is called. Inside it we get the noteid as a string.
* Then the related screen will be searched and found, since the screen expect some arguments at the moment of navigation an
* instance of the bundle class will be created. It contains arguments and stores all objects as KV pairs. By default all the nav params
* are strings*/
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Screen.NotesDisplayScreen.route
    ) {

        composable(Screen.NotesDisplayScreen.route) {
            NoteScreen(
                onNoteClick = {
                    /*paramas are passed through strings as well. If need to pass note id = 5 to the edit screen, we define it by separating slashes like edit_note/5*/
                    navController.navigate(Screen.EditNoteScreen.createRoute(it.id))
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
            val noteId = Screen.EditNoteScreen.getNoteId(it.arguments)
            EditNoteScreen(
                onFinishNoteCreation = {
                    navController.popBackStack()
                },
                noteId = noteId,
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object NotesDisplayScreen : Screen("NotesDisplayScreen")
    data object CreateNoteScreen : Screen("CreateNoteScreen")

    /*Here we pass the note id to the route screen separated by slashes. If we need to add more params define the data class
    * and pass it down*/
    data object EditNoteScreen : Screen("EditNoteScreen/{noteId}") {
        fun createRoute(noteId: Int): String {
            return "EditNoteScreen/$noteId"
        }

        fun getNoteId(arguments: Bundle?): Int {
            return arguments?.getString("noteId")?.toInt() ?: 0
        }
    }
}
