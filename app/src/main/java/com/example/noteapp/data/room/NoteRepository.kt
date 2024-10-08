package com.example.noteapp.data.room

import kotlinx.coroutines.flow.Flow

interface NoteRepository{

    fun getAllNotes(): Flow<List<NoteEntity>>

    fun getNote(id:Long): Flow<NoteEntity?>

    suspend fun insertNote(note: NoteEntity)
    suspend fun deleteNote(note: NoteEntity)
    suspend fun updateNote(note: NoteEntity)
}