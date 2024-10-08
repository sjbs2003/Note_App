package com.example.noteapp.data.room

import kotlinx.coroutines.flow.Flow

class OfflineNoteRepository(private val noteDao: NoteDao): NoteRepository {
    override fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    override fun getNote(id: Long): Flow<NoteEntity?> = noteDao.getNoteById(id)

    override suspend fun insertNote(note: NoteEntity) = noteDao.insert(note)

    override suspend fun updateNote(note: NoteEntity) = noteDao.update(note)

    override suspend fun deleteNote(note: NoteEntity) = noteDao.delete(note)
}