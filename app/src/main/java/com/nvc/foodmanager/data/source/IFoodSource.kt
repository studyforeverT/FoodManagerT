package com.nvc.foodmanager.data.source

import com.nvc.foodmanager.model.Food
import kotlinx.coroutines.flow.Flow

interface IFoodSource {
    fun getDataRemote(): Flow<Result<List<Food>>>
    fun insertFood(food: Food): Flow<Boolean>
    fun editFood(food: Food, image: String?): Flow<Boolean>
    fun deleteFood(food: Food): Flow<Boolean>
}