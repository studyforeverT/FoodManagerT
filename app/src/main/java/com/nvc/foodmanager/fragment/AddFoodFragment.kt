package com.nvc.foodmanager.fragment

import android.Manifest
import android.R
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nvc.foodmanager.databinding.FragmentFoodAddBinding
import com.nvc.foodmanager.model.Category
import com.nvc.foodmanager.model.Food
import com.nvc.foodmanager.viewmodel.CategoryViewModel
import com.nvc.foodmanager.viewmodel.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFoodFragment : Fragment() {
    private lateinit var binding: FragmentFoodAddBinding
    private val viewModel: FoodViewModel by viewModels()
    private val ctgViewModel: CategoryViewModel by viewModels()
    private var option = ""

    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodAddBinding.inflate(layoutInflater)
        ctgViewModel.getAllCategories()


        binding.apply {
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Add Food")
            progressDialog.setMessage("Adding...")
            ctgViewModel.categories.observe(viewLifecycleOwner) {
                val menu: ArrayList<Category> = it as ArrayList<Category>
                spinner.adapter = ArrayAdapter(
                    requireActivity(),
                    R.layout.simple_spinner_item, menu
                )
            }
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    val category: Category? = ctgViewModel.categories.value?.get(position)
                    if (category != null) {
                        option = category.id
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
            imageFood.setOnClickListener {
                checkPermissionPickImage()
            }
            viewModel.apply {
                imageUploadUri.observe(viewLifecycleOwner) {
                    imgFood.setImageURI(it)
                }
                statusAction.observe(viewLifecycleOwner) {
                    if (it) {
                        progressDialog.dismiss()
                        findNavController().popBackStack()
                        Toast.makeText(context, "Add successfully", Toast.LENGTH_LONG).show()
                    }
                }
            }
            btnAdd.setOnClickListener {
                viewModel.setStatusAction(false)
                addFood()
            }
        }

        return binding.root
    }


    private fun addFood() {
        binding.apply {
            val name = edtName.text.toString()
            val description = edtDescription.text.toString()
            val price = if (edtPrice.text.toString() == "") {
                0
            } else {
                edtPrice.text.toString().toInt()
            }
            val foodNew = Food("", name, option, description, "", price)
            if (name == "" || description == "" || option == "" || viewModel.imageUploadUri.value == null) {
                Toast.makeText(context, "Please fill all information", Toast.LENGTH_SHORT).show()
            } else if (price < 10000) {
                Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.show()
                viewModel.insertFood(foodNew)
            }
        }
    }

    private fun checkPermissionPickImage() {
        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat
                .requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100
                )
        } else {
            selectImage();
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 100 && resultCode === Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            Log.d("TAG", uri.toString())
            viewModel.setImageUpload(uri)
        }
    }
}