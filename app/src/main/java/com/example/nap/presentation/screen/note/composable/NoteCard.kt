package com.example.nap.presentation.screen.note.composable

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.nap.domain.model.Note

/*
    Follow these three rules when working with reusable composable functions
    1.Always pass the modifier as the first and a default param. So the component can be modified whenever
    needed without changing the core.
    2.All the data the function works with should come from the outside as parameters. Follow the least knowledge
    principle.
    3.To maintainability and reusability do not hard code what happens to a user react in the composable.
    Instead of that pass a callback function and handle it in the usage. That we do not tie the composable
    to a specific viewmodel.
*/
@Composable
fun NoteCard(
    modifier: Modifier = Modifier, note: Note, onNoteClick: (Note) -> Unit
) {
    Text(
        modifier = modifier.clickable {
            onNoteClick(note)
        }, text = "${note.title} - ${note.content}", fontSize = 24.sp
    )
}
