package com.example.noteapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.noteapp.data.room.NoteEntity
import com.example.noteapp.data.room.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _notes = mutableStateOf<List<NoteEntity>>(emptyList())
    val notes: State<List<NoteEntity>> = _notes

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            _notes.value = repository.getAllNotes()
        }
    }

    fun editNote(note: NoteEntity) {
        viewModelScope.launch {
            val existingNote = repository.getNoteById(note.id)
            if (existingNote != null) {
                // Update the note since it already exists
                repository.update(note)
            } else {
                // Handle the case where the note doesn't exist, you can either show a message
                // or insert the note as a new one
                // For example, you can throw an exception or notify the user
                // e.g., throw IllegalArgumentException("Note not found for editing")
                repository.insert(note) // or notify user if preferred
            }
            getAllNotes() // Refresh the list of notes
        }
    }


    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.delete(note)
            getAllNotes() // Refresh notes after deleting
        }
    }

    fun getNoteById(noteId: Int): NoteEntity? {
        return _notes.value.find { it.id.toInt() == noteId }
    }

}
