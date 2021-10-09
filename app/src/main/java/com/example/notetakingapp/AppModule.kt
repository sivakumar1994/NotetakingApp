package com.example.notetakingapp

import android.app.Application
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {
    @Provides
    @Singleton
    fun provideWebSerice() :WebEndPoint{
        return Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .build()
            .create(WebEndPoint::class.java)
    }
    @Provides
    @Singleton
    fun provideMainActivityRepo(webEndPoint: WebEndPoint) :MainActivityRepo {
        return MainActivityRepo(application,webEndPoint)
    }
}