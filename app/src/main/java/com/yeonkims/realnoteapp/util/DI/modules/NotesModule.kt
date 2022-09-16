package com.yeonkims.realnoteapp.util.DI.modules

import com.yeonkims.realnoteapp.data.impl.fake_repositories.FakeNoteRepository
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
    fun provideNoteRepository(): NoteRepository {
        return FakeNoteRepository()
    }
}