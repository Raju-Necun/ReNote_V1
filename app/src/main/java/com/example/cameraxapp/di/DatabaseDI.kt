package com.example.cameraxapp.di

import android.content.Context
import androidx.room.Room
import com.example.cameraxapp.db.NoteDatabase
import com.example.cameraxapp.utils.Constants.NOTE_DATABASE

fun provideDatabase(context: Context)=
    Room.databaseBuilder(context, NoteDatabase::class.java, NOTE_DATABASE)
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

fun provideDao(db :NoteDatabase) = db.noteDao()