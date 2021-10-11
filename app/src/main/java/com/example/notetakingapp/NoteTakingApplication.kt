package com.example.notetakingapp

import android.app.Application
import com.example.notetakingapp.di.AppComponent
import com.example.notetakingapp.di.AppModule
import com.example.notetakingapp.di.DaggerAppComponent

class NoteTakingApplication : Application(), ComponentProvider {

    override val component: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }


    override fun onCreate() {
        super.onCreate()
    }
}

interface ComponentProvider {
    val component: AppComponent
}
