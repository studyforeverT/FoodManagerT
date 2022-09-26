package com.nvc.foodmanager.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class MyConvert @Inject constructor(private val ctx: Context) {
    fun imageUriToBase64(uri : Uri) : String?{
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(ctx.contentResolver, uri)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bytes: ByteArray = stream.toByteArray()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}