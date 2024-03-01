package com.example.cameraxapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cameraxapp.utils.Constants.NOTE_TABLE
import kotlinx.coroutines.flow.Flow




@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(noteEntity: NoteEntity)

    @Query("SELECT * FROM $NOTE_TABLE ORDER BY noteId DESC")
    fun getAllNotes(): Flow<MutableList<NoteEntity>>

//    // Get notes by folder
//    @Query("SELECT * FROM $NOTE_TABLE WHERE folder_id = :folderId ORDER BY noteId DESC")
//    fun getNotesByFolder(folderId: Int): Flow<List<NoteEntity>>
//
//    // Insert a new folder
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertFolder(folderEntity: FolderEntity): Long

}
