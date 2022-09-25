package com.yeonkims.realnoteapp.util.DI.modules

import android.view.View
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yeonkims.realnoteapp.data.impl.firebase_repositories.FirebaseNoteRepository
import com.yeonkims.realnoteapp.data.impl.temp_repositories.TempNoteRepository
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

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
}

@Module
@InstallIn(FragmentComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideNoteRepository(impl: FirebaseNoteRepository): NoteRepository
}