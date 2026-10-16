package com.example.rickotheque.core.network.graphql.di

import com.apollographql.apollo.ApolloClient
import org.koin.dsl.module

private const val RICK_AND_MORTY_GRAPHQL_ENDPOINT = "https://rickandmortyapi.com/graphql"

/**
 * Shared Apollo `ApolloClient` singleton every feature's `data` module
 * depends on for its GraphQL-backed repository implementation — mirrors
 * `coreNetworkModule`'s Ktor `HttpClient`.
 */
val coreNetworkGraphqlModule =
    module {
        single {
            ApolloClient.Builder()
                .serverUrl(RICK_AND_MORTY_GRAPHQL_ENDPOINT)
                .build()
        }
    }
