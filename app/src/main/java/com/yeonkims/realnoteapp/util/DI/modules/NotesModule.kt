package com.yeonkims.realnoteapp.util.DI.modules

import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.yeonkims.realnoteapp.data.impl.firebase_repositories.FirebaseNoteRepository
import com.yeonkims.realnoteapp.data.impl.temp_repositories.TempNoteRepository
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideFirebaseFunctions(): FirebaseFunctions {
        return Firebase.functions
    }

    @Provides
    @Singleton
    fun provideFirebaseNoteRepository(functions: FirebaseFunctions, gson: Gson): FirebaseNoteRepository {
        return FirebaseNoteRepository(functions, gson)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteRepositoryImpl: FirebaseNoteRepository): NoteRepository {
        return noteRepositoryImpl
    }
}