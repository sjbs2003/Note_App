package com.example.noteapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.room.NoteEntity
import com.example.noteapp.data.room.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteDetailViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _noteState = MutableStateFlow<NoteEntity?>(null)
    val noteState: StateFlow<NoteEntity?> = _noteState.asStateFlow()

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
        _noteState.value = _noteState.value?.copy(title = title)
    }

    fun updateContent(content: String) {
        _noteState.value = _noteState.value?.copy(content = content)
    }

    fun saveNote() {
        viewModelScope.launch {
            _noteState.value?.let { repository.updateNote(it) }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            _noteState.value?.let { repository.deleteNote(it) }
        }
    }

    class DetailViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            return NoteDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
}