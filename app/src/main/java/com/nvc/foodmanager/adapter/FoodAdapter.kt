package com.nvc.foodmanager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.foodmanager.databinding.ItemFoodBinding
import com.nvc.foodmanager.diff.FoodDiff
import com.nvc.foodmanager.listener.ItemListener
import com.nvc.foodmanager.model.Food
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodAdapter  @Inject constructor() : RecyclerView.Adapter<FoodAdapter.ViewHolder>(){
    private val listFood = arrayListOf<Food>()
    private var mItemCategoryListener: ItemListener<Food>? = null

    fun setOnClickListener(onClickListener: ItemListener<Food>) {
        this.mItemCategoryListener = onClickListener
    }


    class ViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) {
            binding.food = food
            binding.executePendingBindings()
        }
    }

    fun submitData(temp: List<Food>) {
        val diff = DiffUtil.calculateDiff(FoodDiff(listFood, temp))
        listFood.clear()
        listFood.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listFood[position].let { ctg ->
            holder.bind(ctg)
            holder.itemView.setOnClickListener {
                mItemCategoryListener?.onClickListener(ctg)
            }
        }
    }

    override fun getItemCount(): Int {
        return listFood.size
    }
}