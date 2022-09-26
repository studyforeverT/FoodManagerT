package com.nvc.foodmanager.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nvc.foodmanager.api.ImgurClient
import com.nvc.foodmanager.data.source.firebase.FoodRemote
import com.nvc.foodmanager.data.source.firebase.OrderRemote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase{
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    @Named("CategoryRef")
    fun provideCategoryRef(firebaseDatabase: FirebaseDatabase): DatabaseReference{
        return firebaseDatabase.getReference("Category")
    }
    @Provides
    @Singleton
    @Named("FoodRef")
    fun provideFoodRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Food")
    }

    @Provides
    @Singleton
    fun provideFoodRemote(
        @Named("FoodRef")
        foodRef: DatabaseReference,
        imgurClient: ImgurClient
    )
            : FoodRemote {
        return FoodRemote(foodRef,imgurClient)
    }

    @Provides
    @Singleton
    @Named("OrderRef")
    fun provideOrderRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Orders")
    }

    @Provides
    @Singleton
    @Named("OrderNotificationRef")
    fun provideOrderNotificationRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Notification/OrderNotification")
    }

    @Provides
    @Singleton
    fun provideOrderRemote(
        @Named("OrderRef")
        orderRef: DatabaseReference,
        @Named("OrderNotificationRef")
        orderNotificationRef : DatabaseReference
    )
            : OrderRemote {
        return OrderRemote(orderRef,orderNotificationRef)
    }



}