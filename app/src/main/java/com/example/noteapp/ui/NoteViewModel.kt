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

    fun saveOrUpdateNote(note: NoteEntity) {  // Refresh notes after saving/updating
        viewModelScope.launch {
            val existingNote = repository.getNoteById(note.id)
            if (existingNote != null){
                repository.update(note) // update existing note
            } else {
                repository.insert(note) // insert new note
            }
            getAllNotes()
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
