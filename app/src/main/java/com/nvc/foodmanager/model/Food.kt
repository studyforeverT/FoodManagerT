package com.nvc.foodmanager.model

import com.nvc.foodmanager.util.Convert.Companion.toMoneyFormat
import java.io.Serializable

data class Food(
    var id: String = "",
    var name: String = "",
    var categoryID: String = "",
    var description: String = "",
    var image: String = "",
    var price: Int = 0
) : Serializable {
    fun convertToMoneyFormat() : String{
        return price.toMoneyFormat()
    }
}