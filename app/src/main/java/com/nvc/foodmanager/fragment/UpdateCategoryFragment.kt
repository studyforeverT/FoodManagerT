package com.gnut.tungpt1_advance_android_day_8.fragment

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nvc.foodmanager.databinding.FragmentUpdateCategoryBinding
import com.nvc.foodmanager.model.Category
import com.nvc.foodmanager.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class UpdateCategoryFragment : Fragment() {
    private lateinit var binding : FragmentUpdateCategoryBinding
    private val viewModel : CategoryViewModel by viewModels()
    private val args : UpdateCategoryFragmentArgs by navArgs()
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateCategoryBinding.inflate(inflater,container,false)
        binding.apply {
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Update Category")
            progressDialog.setMessage("Updating...")
            category = args.category
            btnUpdate.setOnClickListener {
                viewModel.setStatusAction(false)
                updateCategory()
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
                        Toast.makeText(context, "Update successfully", Toast.LENGTH_LONG).show()
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
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 100 && resultCode === Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            viewModel.setImageUpload(uri)
        }
    }

    private fun updateCategory() {
        binding.apply {
            val name = layoutEdtTeaName.editText?.text.toString()
            val description = layoutEdtDescription.editText?.text.toString()
            val category = Category(args.category.id, name, description, args.category.image)
            if (name == "" || description == "") {
                Toast.makeText(context, "Please fill all information", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.show()
                viewModel.updateCategory(category)
            }
        }
    }


}