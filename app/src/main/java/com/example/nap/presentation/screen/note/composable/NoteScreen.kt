package com.example.nap.presentation.screen.note.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nap.presentation.screen.note.viewmodel.NoteScreenState
import com.example.nap.presentation.screen.note.viewmodel.NotesCommands
import com.example.nap.presentation.screen.note.viewmodel.NotesViewModel

@Composable
fun NoteScreen(
//  Modifier should be the first param passed down to a composable function, Default value is Modifier
    modifier: Modifier = Modifier, viewMoel: NotesViewModel = viewModel()
) {

//This state function will automatically access the state.value from the view model
    val state: NoteScreenState by viewMoel.state.collectAsState()

    /*
        The scroll state should survive the recomposition.
        By using the remember function we can avoid that issue.
        The remember function allows us to store values between recompositions.
        But will not save activity sates
     */

    /*
        Use Lazy col when displaing large ammount of data. Use item for each of uniqe composables
    */

    LazyColumn(
        modifier = Modifier.padding(top = 48.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            LazyRow(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.pinnedNotes.forEach { note ->
//                    Use key to bind an item to the object it displays
                    item(key = note.id) {
                        NoteCard(
                            note = note, onNoteClick = {
                                viewMoel.processCommand(NotesCommands.SwitchPinStatus(note.id))
                            })
                    }

                }
            }
        }
        /*
        This is the same as above for each function. Instead of manually iterating we pass the collection to
        the items and let it go through it.
        We use the key to link the item with the ui updates. For example if it wasn't used and updated item will
        show incrrect ui states.
        So the items will properly associate with the objects that they display
        */

        items(items = state.otherNotes, key = { it.id }) { note ->
            NoteCard(note = note, onNoteClick = {
                viewMoel.processCommand(NotesCommands.SwitchPinStatus(note.id))
            })

        }
    }
}
