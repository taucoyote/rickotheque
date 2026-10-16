package com.example.rickotheque.features.location.data.network

import com.example.rickotheque.features.location.domain.model.Location
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/** REST (Ktor) implementation, calling `GET https://rickandmortyapi.com/api/location/{id}`. */
class LocationApiService(private val client: HttpClient) {

    private val gson = Gson()

    suspend fun getLocationById(id: Int): Location? {
        val response: String =
            client.get("https://rickandmortyapi.com/api/location/$id").body()
        return try {
            gson.fromJson(response, Location::class.java)
        } catch (e: JsonSyntaxException) {
            //Log.e("LocationApiService", "Error parsing JSON: ${e.message}", e)
            null
        }
    }
}
