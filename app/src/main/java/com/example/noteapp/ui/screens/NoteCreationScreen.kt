package com.example.noteapp.ui.screens

import android.view.ViewTreeObserver
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.R
import com.example.noteapp.data.room.NoteRepository
import com.example.noteapp.viewModel.NoteCreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCreationScreen(
    repository: NoteRepository,
    onBackClick:() -> Unit
) {
    val viewModel: NoteCreationViewModel = viewModel(
        factory = NoteCreationViewModel.CreationViewModelFactory(repository)
    )
    val noteState by viewModel.noteState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories = listOf("All","Work","Reading","Important" )
    val darkGray = Color(0xFF1E1E1E)
    val keyboardVisible by keyboardAsState()
    var expanded by remember { mutableStateOf(false) }
    
    Scaffold(
        containerColor = darkGray,
        topBar = {
            Column {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.saveNote()
                            onBackClick()
                        }) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Done",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Handle undo */ }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_undo),
                                contentDescription = "Undo",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /* Handle redo */ }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_redo),
                                contentDescription = "Redo",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /* Handle notifications */ }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /* Handle more options */ }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = darkGray
                    ),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = (painterResource(R.drawable.ic_folder)),
                        contentDescription = "Category",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(selectedCategory, color = Color.White)
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Select Category",
                            tint = Color.White
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded =false },
                        modifier = Modifier.background(darkGray)
                    ) {
                        categories.forEach { category->
                            DropdownMenuItem(
                                text = { Text(category, color = Color.White) },
                                onClick = {
                                    viewModel.updateCategory(category)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            KeyboardAwareBottomAppBar(
                visible = keyboardVisible,
                content = { AdaptiveBottomAppBar() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .background(darkGray)
        ) {
            TextField(
                value = noteState.title,
                onValueChange = { viewModel.updateTitle(it) },
                placeholder = { Text("Title", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = darkGray,
                    unfocusedContainerColor = darkGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )
            TextField(
                value = noteState.content,
                onValueChange = { viewModel.updateContent(it) },
                placeholder = { Text("Note content", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = darkGray,
                    unfocusedContainerColor = darkGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
            )
        }
    }
}

@Composable
fun KeyboardAwareBottomAppBar(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val windowInsets = WindowInsets.ime
    val bottomPadding = with(density) { windowInsets.getBottom(density).toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ){
        content()
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {

    val keyboardState = remember { mutableStateOf(false) }
    val view = LocalView.current

    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val insets = ViewCompat.getRootWindowInsets(view)
            keyboardState.value = insets?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }
    return keyboardState
}

@Composable
fun AdaptiveBottomAppBar() {
    val lightGray = Color(0xFF2A2A2A)
    val windowInsets = WindowInsets.navigationBars
    val bottomInsets = with(LocalDensity.current) { windowInsets.getBottom(this).toDp() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp + bottomInsets)
            .padding(bottom = bottomInsets),
        color = lightGray,
        shadowElevation = 8.dp,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Toggle bold */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_formatbold),
                    contentDescription = "Bold",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* Toggle italic */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_formatitalic),
                    contentDescription = "Italic",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* Toggle underline */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_formatunderlined),
                    contentDescription = "Underline",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* Activate speech to text */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_mic),
                    contentDescription = "Speech to Text",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* Add image */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_images),
                    contentDescription = "Add Image",
                    tint = Color.White
                )
            }
        }
    }
}

