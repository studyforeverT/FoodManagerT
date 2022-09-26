package com.nvc.foodmanager.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvc.foodmanager.data.repository.OrderRepository
import com.nvc.foodmanager.model.Food
import com.nvc.foodmanager.model.Order
import com.nvc.foodmanager.model.OrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>>
        get() = _orders

    private val _ordersByStatus = MutableLiveData<List<Order>>()
    val ordersByStatus: LiveData<List<Order>>
        get() = _ordersByStatus

    private val status = MutableLiveData<String>("All")

    private val _listOrderItems = MutableLiveData<ArrayList<OrderItem>>()
    val listOrderItems: LiveData<ArrayList<OrderItem>>
        get() = _listOrderItems


    fun getDataRemote() {
        viewModelScope.launch {
            orderRepository.getDataRemote().collect {
                when {
                    it.isSuccess -> {
                        withContext(Dispatchers.Main) {
                            _orders.value = it.getOrNull()
                            _ordersByStatus.value = it.getOrNull()
                        }
                    }
                }
            }
        }
    }

    fun updateStatus(order: Order, status: Int) {
        viewModelScope.launch {
            order.status = status
            orderRepository.updateStatus(order).collect {
            }
        }
    }

    fun getListOrderItems(orderID: String) {
        viewModelScope.launch {
            _listOrderItems.value = _orders.value!!.find {
                it.id == orderID
            }?.order_items
        }
    }

    fun updateStatus(stt: String) {
        viewModelScope.launch {
            status.value = stt
        }
    }

    fun getListOrderByStatus() {
        viewModelScope.launch {
            when (status.value) {
                "All" -> {
                    getDataRemote()
                }
                "Pending" -> {
                    _orders.value = ordersByStatus.value
                    _orders.value = orders.value?.filter {
                        it.status == 2
                    }
                }
                "Cancel" -> {
                    _orders.value = ordersByStatus.value
                    _orders.value = orders.value?.filter {
                        it.status == 0
                    }
                }
                "Success" -> {
                    _orders.value = ordersByStatus.value
                    _orders.value = orders.value?.filter {
                        it.status == 1
                    }
                }
            }
        }
    }

}