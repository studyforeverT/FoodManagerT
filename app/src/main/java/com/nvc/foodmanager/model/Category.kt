package com.nvc.foodmanager.model

import java.io.Serializable


class Category(
    var id : String="",
    var name : String="",
    var description : String="",
    var image : String="",
) : Serializable{
    override fun toString(): String {
        return name
    }
}