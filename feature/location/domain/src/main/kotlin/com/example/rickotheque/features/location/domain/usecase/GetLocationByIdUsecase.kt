package com.example.rickotheque.features.location.domain.usecase

import com.example.rickotheque.features.location.domain.model.Location
import com.example.rickotheque.features.location.domain.repository.LocationRepository

class GetLocationByIdUseCase(private val repository: LocationRepository) {
    suspend operator fun invoke(id: Int): Location? = repository.getLocationById(id)
}
