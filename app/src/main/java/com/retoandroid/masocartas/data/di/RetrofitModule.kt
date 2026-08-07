package com.retoandroid.masocartas.data.di

import com.retoandroid.masocartas.data.remote.api.ApiYiGiOh
import com.retoandroid.masocartas.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiYuGiOh(retrofit: Retrofit): ApiYiGiOh {
        return retrofit.create(ApiYiGiOh::class.java)
    }

}