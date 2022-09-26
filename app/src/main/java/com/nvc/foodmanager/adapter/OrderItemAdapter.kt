package com.nvc.foodmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.foodmanager.R
import com.nvc.foodmanager.databinding.ItemOrderItemBinding
import com.nvc.foodmanager.diff.OrderItemDiff
import com.nvc.foodmanager.model.OrderItem
import javax.inject.Inject

class OrderItemAdapter @Inject constructor() : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    private val listOrderItems = arrayListOf<OrderItem>()

    class ViewHolder(private val binding: ItemOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItem: OrderItem) {
            binding.item = orderItem
            binding.position = bindingAdapterPosition
            binding.executePendingBindings()
        }
    }

    fun submitData(temp: List<OrderItem>) {
        val diff = DiffUtil.calculateDiff(OrderItemDiff(listOrderItems, temp))
        listOrderItems.clear()
        listOrderItems.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listOrderItems[position].let { orderItem ->
            holder.bind(orderItem)
        }
    }

    override fun getItemCount(): Int {
        return listOrderItems.size
    }

}