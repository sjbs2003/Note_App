package com.example.noteapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.room.NoteEntity
import com.example.noteapp.data.room.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteDetailViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _noteState = MutableStateFlow(NoteEntity(title = "", content = ""))
    val noteState: StateFlow<NoteEntity> = _noteState.asStateFlow()

    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            repository.getNote(noteId).collect { note->
                note?.let {
                    _noteState.value = it
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _noteState.value = _noteState.value.copy(title = title)
    }

    fun updateContent(content: String) {
        _noteState.value = _noteState.value.copy(content = content)
    }

    fun saveNote() {
        viewModelScope.launch {
            repository.updateNote(_noteState.value)
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            repository.deleteNote(_noteState.value)
        }
    }
}
