package com.example.noteapp.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.R
import com.example.noteapp.data.room.NoteRepository
import com.example.noteapp.viewModel.NoteDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Long,
    onBackClick: () -> Unit,
    repository: NoteRepository
) {
    val viewModel: NoteDetailViewModel = viewModel(
        factory = NoteDetailViewModel.DetailViewModelFactory(repository)
    )
    val noteState by viewModel.noteState.collectAsState()
    val darkGray = Color(0xFF1E1E1E)
    val context = LocalContext.current
    val keyboardVisible by keyboardAsState()
    LaunchedEffect(noteId) {
        viewModel.loadNote(noteId)
    }

    Scaffold(
        containerColor = darkGray,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { shareNoteContent(context, noteState.title, noteState.content) }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                    }
                    IconButton(onClick = { viewModel.saveNote() }) {
                        Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White)
                    }
                    IconButton(onClick = {
                        viewModel.deleteNote()
                        onBackClick()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkGray
                )
            )
        },
        bottomBar = {
            KeyboardAwareBottomAppBar(
                visible = keyboardVisible,
                content = { AdaptiveBottomAppBar() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(darkGray)
                .padding(16.dp)
                .imePadding()
        ) {
            BasicTextField(
                value = noteState.title,
                onValueChange = { viewModel.updateTitle(it) },
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = noteState.content,
                onValueChange = { viewModel.updateContent(it) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}


fun shareNoteContent(context: Context, title: String, content: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_TEXT, content)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.share_note)
        )
    )
}