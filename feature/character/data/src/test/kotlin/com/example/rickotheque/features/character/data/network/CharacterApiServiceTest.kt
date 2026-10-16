package com.example.rickotheque.features.character.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterApiServiceTest {

    private fun createMockHttpClient(response: String): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = response,
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        return HttpClient(mockEngine)
    }

    @Test
    fun `getCharacters returns list of characters on success`() = runBlocking {
        val mockResponse = """[
            {"id": 1, "name": "Rick Sanchez", "status": "Alive", "species": "Human", "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg"},
            {"id": 2, "name": "Morty Smith", "status": "Alive", "species": "Human", "image": "https://rickandmortyapi.com/api/character/avatar/2.jpeg"}
        ]"""

        val client = createMockHttpClient(mockResponse)
        val service = CharacterApiService(client)

        val characterIds = "1,2"
        val characters = service.getCharacters(characterIds)

        assertEquals(2, characters.size)
        assertEquals("Rick Sanchez", characters[0].name)
        assertEquals("Morty Smith", characters[1].name)
    }

    @Test
    fun `getCharacters returns empty list on parsing error`() = runBlocking {
        val mockClient = createMockHttpClient("Invalid JSON")

        val service = CharacterApiService(mockClient)

        val characterIds = "1,2"
        val characters = service.getCharacters(characterIds)

        assertEquals(0, characters.size)
    }
}
