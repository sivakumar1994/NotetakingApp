package com.example.notetakingapp

import android.app.Application

class NoteTakingApplication : Application(),ComponentProvider {

    override val component: AppComponent by lazy {
       DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }


    override fun onCreate() {
        super.onCreate()
    }
}
interface ComponentProvider {
    val component : AppComponent
}
