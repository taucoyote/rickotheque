package com.example.rickotheque.features.location.data.repository

import com.example.rickotheque.features.location.data.network.LocationApiService
import com.example.rickotheque.features.location.domain.model.Location
import com.example.rickotheque.features.location.domain.repository.LocationRepository

class LocationRestRepositoryImpl(
    private val locationApiService: LocationApiService,
) : LocationRepository {
    override suspend fun getLocationById(id: Int): Location? = locationApiService.getLocationById(id)
}
