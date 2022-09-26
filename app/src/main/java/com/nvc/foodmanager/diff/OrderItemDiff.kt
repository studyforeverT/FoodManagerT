package com.nvc.foodmanager.diff

import androidx.recyclerview.widget.DiffUtil
import com.nvc.foodmanager.model.OrderItem

class OrderItemDiff(
    private val oldOrderItems: List<OrderItem>,
    private val newOrderItems: List<OrderItem>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldOrderItems.size

    override fun getNewListSize() = newOrderItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldOrderItems[oldItemPosition].id == newOrderItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldOrderItems === newOrderItems
    }
}