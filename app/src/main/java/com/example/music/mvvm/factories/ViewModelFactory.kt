package com.example.music.mvvm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.music.mvvm.data.Repository
import com.example.music.mvvm.view_model.CommonViewModelFactory

class ViewModelFactory(
    private val repository: Repository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommonViewModelFactory::class.java))
            return CommonViewModelFactory(repository) as T
        throw IllegalArgumentException("Unknown model")
    }
}