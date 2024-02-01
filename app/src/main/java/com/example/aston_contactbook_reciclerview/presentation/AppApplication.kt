package com.example.aston_contactbook_reciclerview.presentation

import android.app.Application
import com.example.aston_contactbook_reciclerview._common.deps_inject.AppComponent
import com.example.aston_contactbook_reciclerview._common.deps_inject.DaggerAppComponent

class AppApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .addContext(this)
            .build()
    }
}