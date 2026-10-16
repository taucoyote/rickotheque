package com.example.rickotheque.features.location.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationApiServiceTest {

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
    fun `getLocationById returns the location on success`() = runBlocking {
        val mockResponse = """{
            "id": 3,
            "name": "Citadel of Ricks",
            "type": "Space station",
            "dimension": "unknown",
            "residents": ["https://rickandmortyapi.com/api/character/8"]
        }"""

        val client = createMockHttpClient(mockResponse)
        val service = LocationApiService(client)

        val location = service.getLocationById(3)

        assertEquals("Citadel of Ricks", location?.name)
        assertEquals(1, location?.residents?.size)
    }

    @Test
    fun `getLocationById returns null on parsing error`() = runBlocking {
        val mockClient = createMockHttpClient("Invalid JSON")

        val service = LocationApiService(mockClient)

        val location = service.getLocationById(3)

        assertEquals(null, location)
    }
}
