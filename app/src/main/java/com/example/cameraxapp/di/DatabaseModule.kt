package com.example.cameraxapp.di

import com.example.cameraxapp.db.NoteDao
import com.example.cameraxapp.repository.DatabaseRepository
import com.example.cameraxapp.viewmodel.DatabaseViewmodel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideDao(get()) }
    factory { DatabaseRepository(get()) }
    viewModel() { DatabaseViewmodel(get()) }
}