package com.example.noteapp.data.room

class NoteRepository(private val noteDao: NoteDao) {

    suspend fun insert(note: Note) {
        noteDao.insert(note.toEntity())
    }

    suspend fun update(note: Note) {
        noteDao.update(note.toEntity())
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note.toEntity())
    }

    suspend fun getAllNotes(): List<Note> {
        return noteDao.getAllNotes().map { it.toDomainModel() }
    }

    suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteById(id)?.toDomainModel()
    }

    private fun Note.toEntity(): NoteEntity {
        return NoteEntity(
            id = this.id,
            title = this.title,
            content = this.content,
            creationDate = this.creationDate
        )
    }

    private fun NoteEntity.toDomainModel(): Note {
        return Note(
            id = this.id,
            title = this.title,
            content = this.content,
            creationDate = this.creationDate
        )
    }


}