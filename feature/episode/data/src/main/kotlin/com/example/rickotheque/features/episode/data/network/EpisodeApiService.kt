package com.example.rickotheque.features.episode.data.network

import com.example.rickotheque.features.episode.domain.model.EpisodeResponse
import com.example.rickotheque.features.episode.domain.model.Info
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class EpisodeApiService(private val client: HttpClient) {

    private val gson = Gson()

    suspend fun getEpisodeList(page: Int): EpisodeResponse {
        val response: String =
            client.get("https://rickandmortyapi.com/api/episode?page=$page").body()
        return try {
            val episodeResponse: EpisodeResponse =
                gson.fromJson(response, EpisodeResponse::class.java)
            episodeResponse
        } catch (e: JsonSyntaxException) {
            //Log.e("EpisodeApiService", "Error parsing JSON: ${e.message}", e)
            EpisodeResponse(Info(0, 0, null, null), emptyList())
        }
    }
}
