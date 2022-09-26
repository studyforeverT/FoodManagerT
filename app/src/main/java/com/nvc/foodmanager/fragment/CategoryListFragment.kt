package com.nvc.foodmanager.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nvc.foodmanager.R
import com.nvc.foodmanager.adapter.CategoryAdapter
import com.nvc.foodmanager.api.ImgurClient
import com.nvc.foodmanager.api.UploadImgCallBack
import com.nvc.foodmanager.databinding.FragmentCategoryListBinding
import com.nvc.foodmanager.model.Category
import com.nvc.foodmanager.model.Food
import com.nvc.foodmanager.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class CategoryListFragment : Fragment(R.layout.fragment_category_list),
    CategoryAdapter.ItemCategoryListener {
    private lateinit var binding: FragmentCategoryListBinding
    private val viewModel: CategoryViewModel by activityViewModels()

    @Inject
    lateinit var imgurClient: ImgurClient

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    @SuppressLint("WrongThread")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        categoryAdapter.setOnClickListener(this)
        viewModel.getAllCategories()
        viewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.submitData(it)
        }
        binding.apply {
            rcvListCategories.itemAnimator = null
            rcvListCategories.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rcvListCategories.adapter = categoryAdapter
            btnAdd.setOnClickListener {
                findNavController()
                    .navigate(
                        CategoryListFragmentDirections
                            .actionCategoryListFragmentToAddCategoryFragment()
                    )
            }
            enableSwipeToDeleteAndUndo()
        }
        return binding.root
    }

    override fun onClickCategoryListener(ctg: Category) {
        findNavController().navigate(
            CategoryListFragmentDirections.actionCategoryListFragmentToUpdateCategoryFragment(
                ctg
            )
        )
    }

    private fun enableSwipeToDeleteAndUndo() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedCtg: Category? = viewModel.categories.value?.get(position)
                if (deletedCtg != null) {
                    viewModel.deleteCategory(deletedCtg)
                }
                Snackbar.make(
                    binding.rcvListCategories,
                    "Deleted " + deletedCtg?.name,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(
                        "Undo"
                    ) {
                        if (deletedCtg != null) {
                            viewModel.insertCategory(deletedCtg)
                        }
                    }.show()
            }
        }).attachToRecyclerView(binding.rcvListCategories)
    }
}