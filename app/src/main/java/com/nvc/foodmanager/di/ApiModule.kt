package com.nvc.foodmanager.di

import com.nvc.foodmanager.api.ImgurClient
import com.nvc.foodmanager.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Singleton
    @Provides
    fun provideImgurRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_IMGUR)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}