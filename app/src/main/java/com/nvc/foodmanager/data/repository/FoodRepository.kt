package com.nvc.foodmanager.data.repository

import android.util.Log
import com.nvc.foodmanager.api.ImgurClient
import com.nvc.foodmanager.data.source.IFoodSource
import com.nvc.foodmanager.data.source.firebase.FoodRemote
import com.nvc.foodmanager.model.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodRepository(private val foodRemote: FoodRemote) :
    IFoodSource {
    override fun getDataRemote(): Flow<Result<List<Food>>> {
        return foodRemote.getDataRemote().apply {
            CoroutineScope(Dispatchers.IO).launch {
                foodRemote.getDataRemote().collect {
                    when {
                        it.isSuccess -> {

                        }
                        it.isFailure -> {
                            it.exceptionOrNull()?.printStackTrace()

                        }
                    }
                }
            }
        }
    }

    override fun insertFood(food: Food): Flow<Boolean> {
        return foodRemote.insertFood(food)
    }

    override fun editFood(food: Food, image: String?): Flow<Boolean> {
        return foodRemote.editFood(food, image)
    }

    override fun deleteFood(food: Food): Flow<Boolean> {
        return foodRemote.deleteFood(food)
    }


}