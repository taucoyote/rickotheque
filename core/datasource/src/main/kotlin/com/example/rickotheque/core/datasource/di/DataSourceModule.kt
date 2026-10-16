package com.example.rickotheque.core.datasource.di

import com.example.rickotheque.core.datasource.DataSourceSelector
import org.koin.core.qualifier.named
import org.koin.dsl.module

/** Qualifiers every feature's `data` module uses to bind both transport-specific repository implementations. */
val restQualifier = named("rest")
val graphqlQualifier = named("graphql")

val dataSourceModule =
    module {
        single { DataSourceSelector() }
    }
