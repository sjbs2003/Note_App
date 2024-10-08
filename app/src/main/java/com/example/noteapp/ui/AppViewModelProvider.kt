package com.example.noteapp.ui


import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteapp.NoteApplication
import com.example.noteapp.ui.screens.NoteCreationViewModel
import com.example.noteapp.ui.screens.NoteDetailViewModel
import com.example.noteapp.ui.screens.NoteListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for NoteListViewModel
        initializer {
            NoteListViewModel(noteApplication().container.noteRepository)
        }

        // Initializer for NoteCreationViewModel
        initializer {
            NoteCreationViewModel(noteApplication().container.noteRepository)
        }

        // Initializer for NoteDetailViewModel
        initializer {
            NoteDetailViewModel(noteApplication().container.noteRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [NoteApplication].
 */
fun CreationExtras.noteApplication(): NoteApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NoteApplication)
