package com.example.aston_contactbook_reciclerview._common.deps_inject.modules

import android.content.Context
import com.example.aston_contactbook_reciclerview.data.repositories.ContactBookRepositoryImpl
import com.example.aston_contactbook_reciclerview.presentation._dependensy.ContactBookRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoriesModule {

    @Provides
    fun providesContactBookRepository(
        context: Context
    ): ContactBookRepository {
        return ContactBookRepositoryImpl(context)
    }

}