package com.example.noteapp.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.R
import com.example.noteapp.data.room.NoteRepository

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
    val keyboardOpen by remember { mutableStateOf(false) }
    val keyboardHeight by remember { mutableStateOf(0.dp) }
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
            FloatingBottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .offset(y = if (keyboardOpen) -keyboardHeight else 0.dp)
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "People can be clever as high as the sky, but as long as they don't write, they will be lost in society and from history",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.8f)
                )
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

@Composable
fun FloatingBottomAppBar(modifier: Modifier = Modifier) {
    val lightGray = Color(0xFF2A2A2A)
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        color = lightGray,
        shadowElevation = 8.dp,
        tonalElevation = 8.dp
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                IconButton(onClick = { /* Toggle bold */ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_formatbold),
                        contentDescription = "Bold",
                        tint = Color.White
                    )
                }
            }
            item {
                IconButton(onClick = { /* Toggle italic */ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_formatitalic),
                        contentDescription = "Italic",
                        tint = Color.White
                    )
                }
            }
            item {
                IconButton(onClick = { /* Toggle underline */ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_formatunderlined),
                        contentDescription = "Underline",
                        tint = Color.White
                    )
                }
            }
            item {
                IconButton(onClick = { /* Activate speech to text */ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_mic),
                        contentDescription = "Speech to Text",
                        tint = Color.White
                    )
                }
            }
            item {
                IconButton(onClick = { /* Add image */ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_images),
                        contentDescription = "Add Image",
                        tint = Color.White
                    )
                }
            }
            item {
                IconButton(onClick = { /* Align left */ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_formatalignleft),
                        contentDescription = "Align Left",
                        tint = Color.White
                    )
                }
            }
            item {
                IconButton(onClick = { /* Align center */ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_formataligncenter),
                        contentDescription = "Align Center",
                        tint = Color.White
                    )
                }
            }
            item {
                IconButton(onClick = { /* Align right */ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_formatalignright),
                        contentDescription = "Align Right",
                        tint = Color.White
                    )
                }
            }
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