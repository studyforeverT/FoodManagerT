package com.nvc.foodmanager.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nvc.foodmanager.R
import com.nvc.foodmanager.adapter.OrderItemAdapter
import com.nvc.foodmanager.databinding.FragmentOrderDetailBinding
import com.nvc.foodmanager.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailFragment : Fragment(){
    private lateinit var binding : FragmentOrderDetailBinding
    private val orderDetailViewModel : OrderViewModel by activityViewModels()
    @Inject
    lateinit var orderItemAdapter: OrderItemAdapter
    private val args : OrderDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        orderDetailViewModel.getDataRemote()
        orderDetailViewModel.getListOrderItems(args.order.id)
        binding = FragmentOrderDetailBinding.inflate(layoutInflater)

        binding.apply {
            layoutBt.visibility = if (args.order.status == 2) View.VISIBLE else View.GONE
            layoutStt.visibility = if (args.order.status == 2) View.GONE else View.VISIBLE

            btnSuccess.setOnClickListener {
                orderDetailViewModel.updateStatus(args.order,1)
                Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            btnCancel.setOnClickListener {
                orderDetailViewModel.updateStatus(args.order,0)
                Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            order = args.order
            rcvListFoodOrder.layoutManager = LinearLayoutManager(context)
            rcvListFoodOrder.adapter = orderItemAdapter
            orderDetailViewModel.listOrderItems.observe(viewLifecycleOwner){
                orderItemAdapter.submitData(it)
            }
        }
        return binding.root
    }
}