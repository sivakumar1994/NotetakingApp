package com.example.notetakingapp.di

import android.app.Application
import com.example.notetakingapp.endpoint.WebEndPoint
import com.example.notetakingapp.repository.MainActivityRepo
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {
    @Provides
    @Singleton
    fun provideWebSerice() : WebEndPoint {
        return Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .build()
            .create(WebEndPoint::class.java)
    }
    @Provides
    @Singleton
    fun provideMainActivityRepo(webEndPoint: WebEndPoint) : MainActivityRepo {
        return MainActivityRepo(application,webEndPoint)
    }
}