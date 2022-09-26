package com.nvc.foodmanager.fragment

import android.Manifest
import android.R.attr
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nvc.foodmanager.databinding.FragmentAddCategoryBinding
import com.nvc.foodmanager.model.Category
import com.nvc.foodmanager.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.IOException


@AndroidEntryPoint
class AddCategoryFragment : Fragment() {
    private lateinit var binding: FragmentAddCategoryBinding
    private val viewModel: CategoryViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        binding.apply {
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Add Category")
            progressDialog.setMessage("Adding...")
            btnAdd.setOnClickListener {
                viewModel.setStatusAction(false)
                addCategory()
            }
            cardViewImage.setOnClickListener {
                checkPermissionPickImage()
            }
            viewModel.apply {
                imageUploadUri.observe(viewLifecycleOwner) {
                    imageCategory.setImageURI(it)
                }
                statusAction.observe(viewLifecycleOwner) {
                    if (it) {
                        progressDialog.dismiss()
                        findNavController().popBackStack()
                        Toast.makeText(context, "Add successfully", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return binding.root
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

    private fun addCategory() {
        binding.apply {
            val name = layoutEdtTeaName.editText?.text.toString()
            val description = layoutEdtDescription.editText?.text.toString()
            val category = Category("", name, description, "")
            if (name == "" || description == "" || viewModel.imageUploadUri.value == null) {
                Toast.makeText(context, "Please fill all information", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.show()
                viewModel.insertCategory(category)
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 100 && resultCode === RESULT_OK) {
            val uri: Uri? = data?.data
            viewModel.setImageUpload(uri)
        }
    }

}