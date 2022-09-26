package com.nvc.foodmanager.diff

import androidx.recyclerview.widget.DiffUtil
import com.nvc.foodmanager.model.Food

class FoodDiff(
    private val oldFood: List<Food>,
    private val newFood: List<Food>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldFood.size

    override fun getNewListSize() = newFood.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFood[oldItemPosition].id == newFood[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFood === newFood
    }
}