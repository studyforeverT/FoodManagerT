package com.nvc.foodmanager.fragment

import android.os.Bundle
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
import com.nvc.foodmanager.adapter.FoodAdapter
import com.nvc.foodmanager.databinding.FragmentFoodBinding
import com.nvc.foodmanager.listener.ItemListener
import com.nvc.foodmanager.model.Category
import com.nvc.foodmanager.model.Food
import com.nvc.foodmanager.viewmodel.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FoodFragment : Fragment(), ItemListener<Food> {
    private lateinit var binding: FragmentFoodBinding
    private val viewModel: FoodViewModel by activityViewModels()

    @Inject
    lateinit var foodAdapter: FoodAdapter
    override fun onStart() {
        super.onStart()
        viewModel.getAllFood()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodBinding.inflate(layoutInflater)
        foodAdapter.setOnClickListener(this)
        viewModel.foods.observe(viewLifecycleOwner) {
            foodAdapter.submitData(it)
        }
        binding.apply {
            rcvFood.itemAnimator = null
            rcvFood.layoutManager = LinearLayoutManager(context)
            rcvFood.adapter = foodAdapter
            btnAdd.setOnClickListener {
                findNavController().navigate(FoodFragmentDirections.actionFoodFragmentToAddFoodFragment())
            }
            enableSwipeToDeleteAndUndo()
        }
        return binding.root
    }

    override fun onClickListener(item: Food) {
        findNavController().navigate(
            FoodFragmentDirections.actionFoodFragmentToEditFoodFragment(
                item
            )
        )
    }

    private fun enableSwipeToDeleteAndUndo() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
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
                val deletedFood: Food? = viewModel.foods.value?.get(position)
                if (deletedFood != null) {
                    viewModel.deleteFod(deletedFood)
                }
                Snackbar.make(
                    binding.rcvFood,
                    "Deleted " + deletedFood?.name,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(
                        "Undo"
                    ) {
                        if (deletedFood != null) {
                            viewModel.insertFood(deletedFood)
                        }
                    }.show()
            }
        }).attachToRecyclerView(binding.rcvFood)
    }
}