package com.nvc.foodmanager.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvc.foodmanager.data.repository.CategoryRepository
import com.nvc.foodmanager.model.Category
import com.nvc.foodmanager.utils.MyConvert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository,
    private val myConvert: MyConvert
) :
    ViewModel() {
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _imageUploadUri = MutableLiveData<Uri?>(null)
    val imageUploadUri: LiveData<Uri?>
        get() = _imageUploadUri

    private val _statusAction = MutableLiveData(false)
    val statusAction: LiveData<Boolean>
        get() = _statusAction

    fun setStatusAction(status: Boolean) {
        _statusAction.value = status
    }

    fun setImageUpload(imgUri: Uri?) {
        _imageUploadUri.value = imgUri
    }

    fun getAllCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getAllCategories().collect {
                when {
                    it.isSuccess ->
                        withContext(Dispatchers.Main) {
                            _categories.value = it.getOrNull()
                        }
                }
            }
        }
    }

    fun insertCategory(category: Category) {
        if (category.image != "") {
            viewModelScope.launch {
                repository.addCategory(category, category.image).collect {
                    setStatusAction(true)
                }
            }
        } else {
            val imageBase64 = myConvert.imageUriToBase64(imageUploadUri.value!!)
            if (imageBase64 != null) {
                viewModelScope.launch {
                    repository.addCategory(category, imageBase64).collect {
                        setStatusAction(true)
                    }
                }
            }
        }

    }

    fun updateCategory(category: Category) {
        val imageBase64 = if (imageUploadUri.value != null) {
            myConvert.imageUriToBase64(imageUploadUri.value!!)
        } else null
        viewModelScope.launch {
            repository.updateCategory(category, imageBase64).collect {
                setStatusAction(true)
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category).collect {
                getAllCategories()
            }
        }
    }
}


