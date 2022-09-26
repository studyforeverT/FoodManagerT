package com.nvc.foodmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.foodmanager.R
import com.nvc.foodmanager.databinding.ItemOrderBinding
import com.nvc.foodmanager.diff.OrderDiff
import com.nvc.foodmanager.listener.ItemListener
import com.nvc.foodmanager.model.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderAdapter  @Inject constructor() : RecyclerView.Adapter<OrderAdapter.ViewHolder>(){
    private val listOrder = arrayListOf<Order>()
    private var mItemCategoryListener: ItemListener<Order>? = null

    fun setOnClickListener(onClickListener: ItemListener<Order>) {
        this.mItemCategoryListener = onClickListener
    }


    class ViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.item = order
            binding.status = order.status
            when(order.status){
                0 -> {
                    binding.imgStatus.setImageResource(R.drawable.ic_cancel)
                }
                1 ->  {
                    binding.imgStatus.setImageResource(R.drawable.ic_done)
                }
                2 ->  {
                    binding.imgStatus.setImageResource(R.drawable.ic_pending)
                }
            }
            binding.executePendingBindings()
        }
    }

    fun submitData(temp: List<Order>) {
        val diff = DiffUtil.calculateDiff(OrderDiff(listOrder, temp))
        listOrder.clear()
        listOrder.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listOrder[position].let { ctg ->
            holder.bind(ctg)
            holder.itemView.setOnClickListener {
                mItemCategoryListener?.onClickListener(ctg)
            }
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }
}