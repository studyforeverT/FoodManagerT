package com.nvc.foodmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.foodmanager.databinding.ItemCategoryBinding
import com.nvc.foodmanager.diff.CategoryDiff
import com.nvc.foodmanager.model.Category
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CategoryAdapter @Inject constructor() : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private val listCategories = arrayListOf<Category>()
    private var mItemCategoryListener: ItemCategoryListener? = null

    fun setOnClickListener(onClickListener: ItemCategoryListener) {
        this.mItemCategoryListener = onClickListener
    }

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ctg: Category) {
            binding.category = ctg
            binding.executePendingBindings()
        }
    }

    fun submitData(temp: List<Category>) {
        val diff = DiffUtil.calculateDiff(CategoryDiff(listCategories, temp))
        listCategories.clear()
        listCategories.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listCategories[position].let { ctg ->
            holder.bind(ctg)
            holder.itemView.setOnClickListener {
                mItemCategoryListener?.onClickCategoryListener(ctg)
            }
        }
    }

    override fun getItemCount(): Int {
        return listCategories.size
    }

    interface ItemCategoryListener {
        fun onClickCategoryListener(ctg: Category)
    }

}