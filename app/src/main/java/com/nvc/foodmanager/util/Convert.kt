package com.nvc.foodmanager.util

import java.text.DecimalFormat

class Convert {
    companion object{
        fun Int.toMoneyFormat() : String{
            val m: Double = this.toDouble()
            val formatter = DecimalFormat("###,###,###")
            return formatter.format(m)
        }
    }
}