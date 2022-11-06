package com.yeonkims.realnoteapp.util.DI.modules

import com.google.firebase.auth.FirebaseAuth
import com.yeonkims.realnoteapp.data.impl.firebase_repositories.FirebaseUserRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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