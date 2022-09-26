package com.nvc.foodmanager.data.repository

import com.nvc.foodmanager.data.source.firebase.OrderRemote
import com.nvc.foodmanager.model.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OrderRepository(private val orderRemote: OrderRemote) {
    fun getDataRemote(): Flow<Result<List<Order>>> {
        return orderRemote.getDataRemote()
    }
    fun updateStatus(order: Order) :Flow<Boolean> {
        return orderRemote.updateStatus(order)
    }
}