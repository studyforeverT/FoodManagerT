package com.nvc.foodmanager.diff

import androidx.recyclerview.widget.DiffUtil
import com.nvc.foodmanager.model.Category

class CategoryDiff(
    private val oldCategory: List<Category>,
    private val newCategory: List<Category>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldCategory.size

    override fun getNewListSize() = newCategory.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategory[oldItemPosition].id == newCategory[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategory === newCategory
    }
}