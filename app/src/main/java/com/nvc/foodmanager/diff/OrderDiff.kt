package com.nvc.foodmanager.diff

import androidx.recyclerview.widget.DiffUtil
import com.nvc.foodmanager.model.Order

class OrderDiff(
    private val oldOrder: List<Order>,
    private val newOrder: List<Order>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldOrder.size

    override fun getNewListSize() = newOrder.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldOrder[oldItemPosition].id == newOrder[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldOrder === newOrder
    }
}