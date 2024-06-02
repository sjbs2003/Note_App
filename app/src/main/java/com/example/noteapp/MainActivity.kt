package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.data.room.NoteDatabase
import com.example.noteapp.data.room.NoteRepository
import com.example.noteapp.data.room.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create the repository and factory
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        val repository = NoteRepository(noteDao)
        val viewModelFactory = NoteViewModelFactory(repository)

        setContent {
            val navController = rememberNavController()
            NoteApp(viewModelFactory, navController)
        }
    }
}

@Composable
fun NoteApp(viewModelFactory: NoteViewModelFactory, navController: NavHostController) {
    NoteAppScreen(navController = navController, viewModelFactory = viewModelFactory)
}
