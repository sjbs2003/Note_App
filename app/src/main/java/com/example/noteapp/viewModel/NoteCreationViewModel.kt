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

    class CreationViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteCreationViewModel::class.java)) {
            return NoteCreationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
}