package com.example.rickotheque.features.location.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickotheque.features.location.domain.model.Location
import com.example.rickotheque.features.location.domain.usecase.GetLocationByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val getLocationByIdUseCase: GetLocationByIdUseCase,
) : ViewModel() {
    private val _isLoadingSelectedLocation = MutableStateFlow(false)
    val isLoadingSelectedLocation: StateFlow<Boolean> = _isLoadingSelectedLocation

    private val _selectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation: StateFlow<Location?> = _selectedLocation.asStateFlow()

    /** Reached from a character's location/origin link, via LocationDetailScreen. */
    fun fetchLocationById(locationId: Int) {
        viewModelScope.launch {
            _isLoadingSelectedLocation.value = true
            try {
                _selectedLocation.value = getLocationByIdUseCase(locationId)
            } finally {
                _isLoadingSelectedLocation.value = false
            }
        }
    }
}
