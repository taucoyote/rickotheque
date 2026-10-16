package com.example.rickotheque.features.location.domain.repository

import com.example.rickotheque.features.location.domain.model.Location

interface LocationRepository {
    /** Single location lookup, used to resolve a character's `location`/`origin` link. */
    suspend fun getLocationById(id: Int): Location?
}
