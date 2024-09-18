package com.example.noteapp.data.room

class NoteRepository(private val noteDao: NoteDao) {

    suspend fun insert(note: NoteEntity) {
        noteDao.insert(note.toEntity())
    }

    suspend fun update(note: NoteEntity) {
        noteDao.update(note.toEntity())
    }

    suspend fun delete(note: NoteEntity) {
        noteDao.delete(note.toEntity())
    }

    suspend fun getAllNotes(): List<NoteEntity> {
        return noteDao.getAllNotes().map { it.toDomainModel() }
    }

    suspend fun getNoteById(id: Long): NoteEntity? {
        return noteDao.getNoteById(id)?.toDomainModel()
    }

    private fun NoteEntity.toEntity(): NoteEntity {
        return NoteEntity(
            id = this.id,
            title = this.title,
            content = this.content,
            creationDate = this.creationDate
        )
    }

    private fun NoteEntity.toDomainModel(): NoteEntity {
        return NoteEntity(
            id = this.id,
            title = this.title,
            content = this.content,
            creationDate = this.creationDate
        )
    }


}