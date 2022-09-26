package com.nvc.foodmanager.viewmodel

import android.net.Uri
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.nvc.foodmanager.data.repository.FoodRepository
import com.nvc.foodmanager.model.Category
import com.nvc.foodmanager.model.Food
import com.nvc.foodmanager.utils.MyConvert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val myConvert: MyConvert
) : ViewModel() {
    private val _foods = MutableLiveData<List<Food>>()
    val foods: LiveData<List<Food>>
        get() = _foods
    private val _imageUploadUri = MutableLiveData<Uri?>(null)
    val imageUploadUri: LiveData<Uri?>
        get() = _imageUploadUri

    private val _statusAction = MutableLiveData(false)
    val statusAction: LiveData<Boolean>
        get() = _statusAction

    fun setImageUpload(imgUri: Uri?) {
        _imageUploadUri.value = imgUri
    }

    fun setStatusAction(status: Boolean) {
        _statusAction.value = status
    }

    fun getAllFood() {
        CoroutineScope(Dispatchers.IO).launch {
            foodRepository.getDataRemote().collect {
                when {
                    it.isSuccess -> {
                        withContext(Dispatchers.Main) {
                            _foods.value = it.getOrNull()
                        }
                    }
                }
            }
        }
    }

    fun editFood(food: Food) {
        val imageBase64 = if (imageUploadUri.value != null) {
            myConvert.imageUriToBase64(imageUploadUri.value!!)
        } else null
        viewModelScope.launch {
            foodRepository.editFood(food, imageBase64).collect {
                setStatusAction(true)
            }
        }
    }

    fun insertFood(food: Food) {
        if (food.image != "") {
            viewModelScope.launch {
                foodRepository.insertFood(food).collect {
                    setStatusAction(true)
                }
            }
        } else {
            val imageBase64 = myConvert.imageUriToBase64(imageUploadUri.value!!)
            if (imageBase64 != null) {
                viewModelScope.launch {
                    food.image = imageBase64
                    foodRepository.insertFood(food).collect {
                        setStatusAction(true)
                    }
                }
            }
        }

    }

    fun deleteFod(food: Food) {
        viewModelScope.launch {
            foodRepository.deleteFood(food).collect {
                getAllFood()
            }
        }
    }
}