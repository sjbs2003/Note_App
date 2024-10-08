package com.example.noteapp.ui.screens


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.room.NoteEntity
import com.example.noteapp.data.room.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class NoteCreationViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _noteState = MutableStateFlow(NoteEntity(title = "", content = ""))
    val noteState: StateFlow<NoteEntity> = _noteState.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Work")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun updateTitle(title: String) {
        _noteState.value = _noteState.value.copy(title = title)
    }

    fun updateContent(content: String) {
        _noteState.value = _noteState.value.copy(content = content)
    }

    fun updateCategory(category: String) {
        _selectedCategory.value = category
    }

    fun saveNote() {
        viewModelScope.launch {
            repository.insertNote(_noteState.value)
        }
    }

}
