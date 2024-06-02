package com.example.noteapp.data.room

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val creationDate: Long = System.currentTimeMillis()
)