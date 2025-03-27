package com.dedinurdin.lacakiotakehometest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dedinurdin.lacakiotakehometest.data.repository.FleetRepository

class FleetViewModelFactory(
    private val repository: FleetRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FleetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FleetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}