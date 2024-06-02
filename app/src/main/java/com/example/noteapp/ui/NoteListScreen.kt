package com.example.noteapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteapp.R
import com.example.noteapp.data.room.Note

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit // Add this parameter
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.notes)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddNoteClick() }) { // Call onAddNoteClick
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) {padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(notes){ note ->
                NoteListItem(note = note, onClick = { onNoteClick(note) })
            }
        }
    }
}



@Composable
fun NoteListItem(note: Note, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.headlineMedium)
            Text(text = note.content, maxLines = 1, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview
@Composable
private fun NoteListScreenPreview() {
    val notes = listOf(
        Note(1, "Title 1", "Content 1"),
        Note(2, "Title 2", "Content 2")
    )
    NoteListScreen(notes = notes, onNoteClick = {}, onAddNoteClick = {})
}
