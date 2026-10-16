package com.example.rickotheque.features.character.data.network

import com.example.rickotheque.features.character.domain.model.Characters
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class CharacterApiService(private val client: HttpClient) {

    private val gson = Gson()

    suspend fun getCharacters(characterListIds: String): List<Characters> {
        val response: String =
            client.get("https://rickandmortyapi.com/api/character/$characterListIds").body()
        return try {
            val characterListType = object : TypeToken<List<Characters>>() {}.type
            val characterList: List<Characters> =
                gson.fromJson(response, characterListType)
            characterList
        } catch (_: JsonSyntaxException) {
            //Log.e("CharacterApiService", "Error parsing JSON: ${e.message}", e)
            emptyList()
        } catch (_: IllegalStateException) {
            //Log.e("CharacterApiService", "Error parsing JSON: ${e.message}", e)
            emptyList()
        }
    }
}
