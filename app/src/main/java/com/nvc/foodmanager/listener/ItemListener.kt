package com.nvc.foodmanager.listener

interface ItemListener<T> {
    fun onClickListener(item: T)
    interface ItemDetailOrderListener {
        fun ItemOnClickStatus(status: Int)
    }
}