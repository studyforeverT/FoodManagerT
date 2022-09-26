package com.nvc.foodmanager.api

import retrofit2.Retrofit
import javax.inject.Inject

class ImgurClient @Inject constructor(private val retrofit: Retrofit) {
    val imgurService : ImgurService
    get(){
        return retrofit.create(ImgurService::class.java)
    }
}