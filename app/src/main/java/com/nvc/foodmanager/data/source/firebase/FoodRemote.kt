package com.nvc.foodmanager.data.source.firebase

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.foodmanager.api.ImgurClient
import com.nvc.foodmanager.data.source.IFoodSource
import com.nvc.foodmanager.model.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodRemote(private val foodRef: DatabaseReference, private val imgurClient: ImgurClient) :
    IFoodSource {
    override fun getDataRemote() = callbackFlow<Result<List<Food>>> {
        val foodListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { ds ->
                    ds.getValue(Food::class.java)
                }
                this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()))
            }
        }
        foodRef.addValueEventListener(foodListener)
        awaitClose {
            foodRef.removeEventListener(foodListener)
        }
    }

    override fun insertFood(food: Food) = callbackFlow<Boolean> {
        uploadImage(food.image).collect {
            food.image = it.toString()
            if (it != null) {
                food.image = it
                val taskSuccess = OnSuccessListener<Void> {
                    trySendBlocking(true)
                }
                val taskFailure = OnFailureListener {
                    trySendBlocking(false)
                }
                if (food.id == "") {
                    foodRef.push().apply {
                        CoroutineScope(Dispatchers.IO).launch {
                            food.id = key!!
                            setValue(food)
                                .addOnSuccessListener(taskSuccess)
                                .addOnFailureListener(taskFailure)
                        }
                    }
                } else {
                    foodRef.child(food.id).apply {
                        CoroutineScope(Dispatchers.IO).launch {
                            setValue(food)
                                .addOnSuccessListener(taskSuccess)
                                .addOnFailureListener(taskFailure)
                        }
                    }
                }
            } else {
                trySendBlocking(false)
            }
        }
        awaitClose {
            //remove callback
        }
    }

    override fun editFood(food: Food, image: String?) = callbackFlow<Boolean> {
        fun editLoad() {
            val taskSuccess = OnSuccessListener<Void> {
                trySendBlocking(true)
            }
            val taskFailure = OnFailureListener {
                trySendBlocking(false)
            }
            foodRef.child(food.id)
                .updateChildren(
                    mapOf(
                        "categoryID" to food.categoryID,
                        "description" to food.description,
                        "image" to food.image,
                        "name" to food.name,
                        "price" to food.price
                    )
                )
                .addOnSuccessListener(taskSuccess)
                .addOnFailureListener(taskFailure)
        }
        if (image != null) {
            uploadImage(image).collect {
                if (it != null) {
                    food.image = it
                    editLoad()

                } else {
                    trySendBlocking(false)
                }
            }
        } else {
            editLoad()
        }
        awaitClose {
            //remove callback
        }
    }

    override fun deleteFood(food: Food) = callbackFlow<Boolean> {
        val taskSuccess = OnSuccessListener<Void> {
            trySendBlocking(true)
        }
        val taskFailure = OnFailureListener {
            trySendBlocking(false)
        }

        foodRef.child(food.id)
            .removeValue()
            .addOnSuccessListener(taskSuccess)
            .addOnFailureListener(taskFailure)
        awaitClose {
            //remove callback
        }
    }

    private fun uploadImage(image: String) = callbackFlow<String?> {
        val callbackUpload = object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    response.body()?.let {
                        val link = JSONObject(it.string())
                            .getJSONObject("data").getString("link")
                        trySendBlocking(link)
                    }

                } catch (e: Exception) {
                    trySendBlocking(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("TAGGG", t.toString())
            }
        }
        imgurClient.imgurService.postImage(image).enqueue(callbackUpload)
        awaitClose { }
    }
}