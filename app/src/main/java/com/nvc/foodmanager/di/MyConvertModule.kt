package com.nvc.foodmanager.di

import android.app.Application
import com.nvc.foodmanager.utils.MyConvert
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class MyConvertModule {
    @Provides
    @Singleton
   fun pr0videMyConvert(app : Application) : MyConvert{
       return MyConvert(app.applicationContext)
   }
}