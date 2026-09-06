package com.example.nap.presentation.screen.note.composable

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier,
    note: Note,
    backgroundColor: Color,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
    onDoubleClick: (Note) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .combinedClickable(onClick = {
                onNoteClick(note)
            }, onLongClick = { onLongClick(note) }, onDoubleClick = {
                onDoubleClick(note)
            })
            .padding(16.dp)
    ) {
        Text(
            text = note.title, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface
        )
//        Instead of applying padding, margins to elements which can cause incontinent issue use spacers
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = note.updatedAt.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = note.title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}
