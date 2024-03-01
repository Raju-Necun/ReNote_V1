package com.example.cameraxapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope

import com.example.cameraxapp.db.NoteEntity
import com.example.cameraxapp.repository.DatabaseRepository
import kotlinx.coroutines.launch

class DatabaseViewmodel(private val repository: DatabaseRepository): ViewModel() {

    private val _noteList= MutableLiveData<List<NoteEntity>>()
    val noteList : LiveData<List<NoteEntity>>
        get() = _noteList



    fun getAllNote()= viewModelScope.launch {
        repository.getAllNotes().collect{
            _noteList.postValue(it)
        }
    }

    fun saveNote(noteEntity: NoteEntity)= viewModelScope.launch {
        repository.saveNote(noteEntity)
    }

//    private val _currentFolderId = MutableLiveData<Int>()
//    val currentFolderId: LiveData<Int> = _currentFolderId
//
//    // Flow of notes in the current folder
//    val notesInCurrentFolder: LiveData<List<NoteEntity>> = _currentFolderId.switchMap { folderId ->
//        repository.getNotesByFolder(folderId).asLiveData()
//    }
//
//    // Function to be called when a new folder is selected
//    fun setCurrentFolder(folderId: Int) {
//        _currentFolderId.value = folderId
//    }
//
//    // Function to insert a new folder
//    fun insertFolder(folderName: String) {
//        // Create a new FolderEntity and insert it
//        viewModelScope.launch {
//            val newFolder = FolderEntity(folderName = folderName)
//            val newFolderId = repository.insertFolder(newFolder)
//            // Optionally update the current folder
//            _currentFolderId.value = newFolderId.toInt()
//        }
//    }
//
//    // Add additional ViewModel operations for updating and deleting folders if needed.
//    // ...



    }



