package com.nvc.foodmanager.model

import com.nvc.foodmanager.util.Convert.Companion.toMoneyFormat
import java.io.Serializable

data class OrderItem(
    val id : String="",
    val orderID : String="",
    val foodID : String="",
    val name : String="",
    val image : String="",
    val price : Int=0,
    val quantity : Int=0
): Serializable {
    fun convertToMoneyFormat() : String{
        return price.toMoneyFormat()
    }
}