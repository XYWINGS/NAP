package com.example.nap.presentation.screen.note_display.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nap.domain.model.Note
import com.example.nap.presentation.screen.note_display.viewmodel.NoteScreenState
import com.example.nap.presentation.screen.note_display.viewmodel.NotesCommands
import com.example.nap.presentation.screen.note_display.viewmodel.NotesViewModel
import com.example.nap.presentation.ui.theme.OtherNotesColor
import com.example.nap.presentation.ui.theme.PinnedNotesColor

@Composable
fun NoteScreen(
//  Modifier should be the first param passed down to a composable function, Default value is Modifier
    modifier: Modifier = Modifier,
    viewMoel: NotesViewModel = viewModel(),
    onNoteClick: (Note) -> Unit,
    onFloatingActionButtonClick: () -> Unit
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

    /*Use content padding to prevent certain elements from cutting from up or down. Apply the inner padding
    * from the scaffold to the columns content paddings*/
    Scaffold(
        modifier = modifier, floatingActionButton = {
            FloatingActionButton(
                onClick = onFloatingActionButtonClick,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Button add note",
//                    If you have imported the required icon as a vector use it using the painter,
//                    painter = painterResource(R.drawable._ID_OF_ICON)
                )
            }
        }) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
        ) {
            item {
                Title(
                    modifier = Modifier.padding(horizontal = 24.dp), text = "All Notes"
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                SearchBar(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    query = state.query,
                    onQueryChange = {
                        viewMoel.processCommand(NotesCommands.InputSearchQuery(it))
                    })
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                if (state.pinnedNotes.isNotEmpty()) Subtitle(
                    modifier = Modifier.padding(horizontal = 24.dp), text = "Pinned Notes"
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
//              Use key to bind an item to the object it displays
                    itemsIndexed(
                        items = state.pinnedNotes, key = { _, note -> note.id }) { index, note ->
                        NoteCard(
                            modifier = Modifier.widthIn(max = 160.dp),
                            note = note,
                            backgroundColor = PinnedNotesColor[index % PinnedNotesColor.size],
                            onNoteClick = onNoteClick,
                            onLongClick = {
                                viewMoel.processCommand(NotesCommands.SwitchPinStatus(note.id))
                            },
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                if (state.pinnedNotes.isNotEmpty()) Subtitle(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "Others"
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }/*
            This is the same as above for each function. Instead of manually iterating we pass the collection to
            the items and let it go through it.
            We use the key to link the item with the ui updates. For example if it wasn't used and updated item will
            show incrrect ui states.
            So the items will properly associate with the objects that they display
            *//*
             Define an itemsIndexed when you need to access the index of an item. Ex: Applying colors
             based on the index
             */
            itemsIndexed(items = state.otherNotes, key = { _, note -> note.id }) { index, note ->
                NoteCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    note = note,
                    onNoteClick = onNoteClick,
                    backgroundColor = OtherNotesColor[index % OtherNotesColor.size],
                    onLongClick = {
                        viewMoel.processCommand(NotesCommands.SwitchPinStatus(note.id))
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
