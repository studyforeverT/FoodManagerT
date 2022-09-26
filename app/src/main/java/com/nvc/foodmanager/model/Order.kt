package com.nvc.foodmanager.model

import com.google.firebase.database.PropertyName
import com.nvc.foodmanager.util.Convert.Companion.toMoneyFormat
import java.io.Serializable

data class Order(
    @PropertyName("address") val address: String = "",
    @PropertyName("id") val id: String = "",
    @PropertyName("order_items")
    var order_items: ArrayList<OrderItem> = arrayListOf(),
    @PropertyName("phone") val phone: String = "",
    @PropertyName("receiverName") val receiverName: String = "",
    @PropertyName("status") var status: Int = 0,
    @PropertyName("timestamp") val timestamp: String = "",
    @PropertyName("totalPrice") val totalPrice: Int = 0,
    @PropertyName("userID") val userID: String = "",
) : Serializable {
    fun convertToMoneyFormat(): String {
        return totalPrice.toMoneyFormat()
    }

    fun getSizeItem(): String {
        return order_items.size.toString()
    }

    fun status(): String {
        return when (status) {
            0 -> return "Cancel"
            1 -> return "Success"
            else -> "Cancel"
        }
    }
}

