package com.yeonkims.realnoteapp.DI.modules

import com.yeonkims.realnoteapp.data.impl.fake_repositories.FakeNoteRepository
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object NotesModule {

    @Provides
    fun provideNoteRepository(): NoteRepository {
        return FakeNoteRepository()
    }
}