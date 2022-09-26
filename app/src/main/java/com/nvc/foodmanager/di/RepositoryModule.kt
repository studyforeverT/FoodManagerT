package com.nvc.foodmanager.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nvc.foodmanager.api.ImgurClient
import com.nvc.foodmanager.data.repository.CategoryRepository
import com.nvc.foodmanager.data.repository.FoodRepository
import com.nvc.foodmanager.data.repository.OrderRepository
import com.nvc.foodmanager.data.source.IFoodSource
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
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(
        @Named("CategoryRef") categoryRef: DatabaseReference,
        imgurClient: ImgurClient
    )
            : CategoryRepository {
        return CategoryRepository(categoryRef,imgurClient)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(foodRemote: FoodRemote): FoodRepository {
        return FoodRepository(foodRemote)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(orderRemote: OrderRemote): OrderRepository {
        return OrderRepository(orderRemote)
    }
}