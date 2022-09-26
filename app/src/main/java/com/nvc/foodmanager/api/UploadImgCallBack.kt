package com.nvc.foodmanager.api

import android.util.Log
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UploadImgCallBack : Callback<ResponseBody> {
    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        try {
            response.body()?.let {
                val objUser = JSONObject(it.string()).getJSONObject("data").getString("link")
            }

        } catch (e: Exception) {
            Log.d("TAGGG",e.toString())
        }
    }

    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        Log.d("TAGGG",t.toString())
    }
}