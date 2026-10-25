package com.example.nap.presentation.screen.note_display.composable

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.nap.domain.model.ContentItem
import com.example.nap.domain.model.Note
import com.example.nap.presentation.utils.DateFormatter

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
fun NoteCardWithImage(
    modifier: Modifier = Modifier,
    note: Note,
    imageUrl: String,
    backgroundColor: Color,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .combinedClickable(
                onClick = {
                    onNoteClick(note)
                },
                onLongClick = { onLongClick(note) },
            )
    ) {
        Box {
            AsyncImage(
                modifier = Modifier
                    .heightIn(max = 120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                model = imageUrl,
                contentDescription = "First image from the note",
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = note.title,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )
                Text(
                    text = DateFormatter.formatterDateToString(note.updatedAt),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        /*We just filter the text content from theContentItem and display them */
        note.content
            .filterIsInstance<ContentItem.Text>()
            .filter { it.content.isNotBlank() }
            .joinToString("\n") { it.content }
            .takeIf { it.isNotBlank() }
            ?.let {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = it,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }

    }
}
