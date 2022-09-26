package com.nvc.foodmanager.data.source

import com.nvc.foodmanager.model.Category
import kotlinx.coroutines.flow.Flow

interface ICategorySource {
    fun addCategory(ctg : Category, image : String) : Flow<Boolean>
    fun updateCategory(ctg : Category, image : String?) : Flow<Boolean>
    fun getAllCategories() : Flow<Result<List<Category>>>
    fun deleteCategory(category: Category) : Flow<Boolean>
}