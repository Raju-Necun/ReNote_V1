package com.example.cameraxapp.repository

import com.example.cameraxapp.db.NoteDao
import com.example.cameraxapp.db.NoteEntity
import kotlinx.coroutines.flow.Flow


class DatabaseRepository(private val dao: NoteDao) {

    suspend fun saveNote (noteEntity: NoteEntity) = dao.saveNote(noteEntity)


    fun getAllNotes() = dao.getAllNotes()


}