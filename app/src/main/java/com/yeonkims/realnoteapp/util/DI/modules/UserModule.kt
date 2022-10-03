package com.yeonkims.realnoteapp.util.DI.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.yeonkims.realnoteapp.data.impl.firebase_repositories.FirebaseNoteRepository
import com.yeonkims.realnoteapp.data.impl.firebase_repositories.FirebaseUserRepository
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideFirebaseFunctions(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideNoteRepository(impl: FirebaseUserRepository): UserRepository
}