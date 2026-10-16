package com.example.rickotheque.features.episode.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class EpisodeApiServiceTest {

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
    fun `getEpisodeList returns list of episodes on success`() = runBlocking {
        val mockResponse = """{
            "results": [
                {"id": 1, "name": "Pilot", "air_date": "December 2, 2013", "episode": "S01E01"},
                {"id": 2, "name": "Lawnmower Dog", "air_date": "December 9, 2013", "episode": "S01E02"}
            ]
        }"""

        val client = createMockHttpClient(mockResponse)
        val service = EpisodeApiService(client)

        val episodes = service.getEpisodeList(1)

        assertEquals(2, episodes.results.size)
        assertEquals("Pilot", episodes.results[0].name)
        assertEquals("Lawnmower Dog", episodes.results[1].name)
    }

    @Test
    fun `getEpisodeList returns empty list on parsing error`() = runBlocking {
        val mockClient = createMockHttpClient("Invalid JSON")

        val service = EpisodeApiService(mockClient)

        val episodes = service.getEpisodeList(1)

        assertEquals(0, episodes.results.size)
    }
}
