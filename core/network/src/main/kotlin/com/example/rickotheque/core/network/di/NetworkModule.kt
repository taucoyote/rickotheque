package com.example.rickotheque.core.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

/**
 * Shared Ktor `HttpClient` singleton every feature's `data` module depends
 * on instead of constructing its own — keeps a single connection pool for
 * the whole app.
 */
val coreNetworkModule =
    module {
        single { HttpClient(CIO) }
    }
