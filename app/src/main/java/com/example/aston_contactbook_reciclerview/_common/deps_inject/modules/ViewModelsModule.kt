package com.example.aston_contactbook_reciclerview._common.deps_inject.modules

import com.example.aston_contactbook_reciclerview.presentation.MainViewModel
import com.example.aston_contactbook_reciclerview.presentation._dependensy.ContactBookRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelsModule {

    @Provides
    @Singleton
    fun providesMainViewModule(
        repository: ContactBookRepository,
    ): MainViewModel {
        return MainViewModel(repository = repository)
    }

}