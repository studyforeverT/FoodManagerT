package com.nvc.foodmanager.data.repository

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.nvc.foodmanager.api.ImgurClient
import com.nvc.foodmanager.api.UploadImgCallBack
import com.nvc.foodmanager.data.source.ICategorySource
import com.nvc.foodmanager.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CategoryRepository(
    private val categoryRef: DatabaseReference,
    private val imgurClient: ImgurClient
) : ICategorySource {

    override fun addCategory(ctg: Category, image: String) = callbackFlow<Boolean> {
        CoroutineScope(Dispatchers.IO).launch {
            uploadImage(image).collect {
                if (it != null) {
                    ctg.image = it
                    val taskSuccess = OnSuccessListener<Void> {
                        trySendBlocking(true)
                    }
                    val taskFailure = OnFailureListener {
                        trySendBlocking(false)
                    }
                    if (ctg.id == "") {
                        categoryRef.push().apply {
                            ctg.id = key!!
                            setValue(
                                ctg
                            ).addOnSuccessListener(taskSuccess)
                                .addOnFailureListener(taskFailure)
                        }
                    } else {
                        categoryRef.child(ctg.id).apply {
                            setValue(
                                ctg
                            ).addOnSuccessListener(taskSuccess)
                                .addOnFailureListener(taskFailure)
                        }
                    }

                } else {
                    trySendBlocking(false)
                }
            }
        }
        awaitClose { }
    }

    override fun updateCategory(ctg: Category, image: String?) = callbackFlow<Boolean> {
        CoroutineScope(Dispatchers.IO).launch {
            fun uploadData() {
                val taskSuccess = OnSuccessListener<Void> {
                    trySendBlocking(true)
                }
                val taskFailure = OnFailureListener {
                    trySendBlocking(false)
                }
                categoryRef.child(ctg.id)
                    .setValue(
                        ctg
                    ).addOnSuccessListener(taskSuccess)
                    .addOnFailureListener(taskFailure)
            }
            if (image != null) {
                uploadImage(image).collect {
                    if (it != null) {
                        ctg.image = it
                        uploadData()

                    } else {
                        trySendBlocking(false)
                    }
                }
            } else {
                uploadData()
            }

        }
        awaitClose { }
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

    override fun getAllCategories() =
        callbackFlow<Result<List<Category>>> {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = snapshot.children.map {
                        it.getValue(Category::class.java)
                    }
                    this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()))
                }

                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                }
            }
            categoryRef.addValueEventListener(listener)
            awaitClose {
                categoryRef.removeEventListener(listener)
            }
        }

    override fun deleteCategory(category: Category) = callbackFlow<Boolean> {
        val taskSuccess = OnSuccessListener<Void> {
            trySendBlocking(true)
        }
        val taskFailure = OnFailureListener {
            trySendBlocking(false)
        }
        categoryRef.child(category.id)
            .setValue(null).addOnSuccessListener(taskSuccess)
            .addOnFailureListener(taskFailure)
        awaitClose { }
    }

}

