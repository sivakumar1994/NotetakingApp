package com.example.notetakingapp.di

import com.example.notetakingapp.ui.viewmodel.MainActivityViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivityViewModel: MainActivityViewModel)
}