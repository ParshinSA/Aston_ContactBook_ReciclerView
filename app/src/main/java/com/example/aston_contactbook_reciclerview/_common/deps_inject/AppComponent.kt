package com.example.aston_contactbook_reciclerview._common.deps_inject

import android.content.Context
import com.example.aston_contactbook_reciclerview._common.deps_inject.modules.RepositoriesModule
import com.example.aston_contactbook_reciclerview._common.deps_inject.modules.ViewModelsModule
import com.example.aston_contactbook_reciclerview.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RepositoriesModule::class,
        ViewModelsModule::class,
    ]
)
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun addContext(context: Context): Builder

        fun build(): AppComponent

    }
}