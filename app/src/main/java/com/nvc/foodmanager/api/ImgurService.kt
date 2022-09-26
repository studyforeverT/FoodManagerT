package com.nvc.foodmanager.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ImgurService {
    @POST("/3/image")
    @Headers("Authorization: Client-ID 9dc104b0b628b7e")
    @FormUrlEncoded
    fun postImage(
        @Field("image") image: String,
    ): Call<ResponseBody>
}