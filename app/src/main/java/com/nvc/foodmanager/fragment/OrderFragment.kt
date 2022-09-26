package com.nvc.foodmanager.fragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nvc.foodmanager.adapter.OrderAdapter
import com.nvc.foodmanager.databinding.FragmentOrderBinding
import com.nvc.foodmanager.listener.ItemListener
import com.nvc.foodmanager.model.Order
import com.nvc.foodmanager.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class OrderFragment : Fragment(), ItemListener<Order> {
    private lateinit var binding: FragmentOrderBinding
    private val viewModel: OrderViewModel by activityViewModels()

    @Inject
    lateinit var orderAdapter: OrderAdapter
    private var option = ""
    val menu = arrayListOf("All", "Pending", "Cancel", "Success")

    override fun onResume() {
        super.onResume()
        binding.spinner.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.simple_spinner_item, menu
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getDataRemote()
        binding = FragmentOrderBinding.inflate(layoutInflater)
        orderAdapter.setOnClickListener(this)
        viewModel.orders.observe(viewLifecycleOwner) {
            orderAdapter.submitData(it)
        }
        binding.apply {
            rcvOrders.itemAnimator = null
            rcvOrders.layoutManager = LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
            }
            rcvOrders.adapter = orderAdapter

            spinner.adapter = ArrayAdapter(
                requireActivity(),
                R.layout.simple_spinner_item, menu
            )

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?, position: Int, id: Long
                ) {
                    option = menu[position]
                    viewModel.updateStatus(option)
                    viewModel.getListOrderByStatus()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            swipeLayout.setOnRefreshListener {
                refreshData()
            }
        }
        return binding.root
    }

    override fun onClickListener(item: Order) {
        findNavController().navigate(
            OrderFragmentDirections.actionOrderFragmentToOrderDetailFragment(
                item
            )
        )
    }

    private fun refreshData() {
        binding.swipeLayout.visibility = View.INVISIBLE
        lifecycleScope.launch(Dispatchers.Main) {
            val deferredList = listOf(
                async(Dispatchers.IO) {
                    delay(600)
                },
                async(Dispatchers.Main) {
                    viewModel.getDataRemote()
                }
            )
            binding.spinner.adapter = ArrayAdapter(
                requireActivity(),
                R.layout.simple_spinner_item, menu
            )
            deferredList.awaitAll()
            binding.swipeLayout.visibility = View.VISIBLE
            binding.swipeLayout.isRefreshing = false

        }
    }
}