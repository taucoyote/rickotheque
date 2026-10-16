package com.example.rickotheque.features.location.data.repository

import com.example.rickotheque.features.location.data.network.LocationGraphqlApiService
import com.example.rickotheque.features.location.domain.model.Location
import com.example.rickotheque.features.location.domain.repository.LocationRepository

class LocationGraphqlRepositoryImpl(
    private val locationGraphqlApiService: LocationGraphqlApiService,
) : LocationRepository {
    override suspend fun getLocationById(id: Int): Location? = locationGraphqlApiService.getLocationById(id)
}
