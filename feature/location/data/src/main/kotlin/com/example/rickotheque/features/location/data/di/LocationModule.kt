package com.example.rickotheque.features.location.data.di

import com.example.rickotheque.core.datasource.di.graphqlQualifier
import com.example.rickotheque.core.datasource.di.restQualifier
import com.example.rickotheque.features.location.data.network.LocationApiService
import com.example.rickotheque.features.location.data.network.LocationGraphqlApiService
import com.example.rickotheque.features.location.data.repository.LocationGraphqlRepositoryImpl
import com.example.rickotheque.features.location.data.repository.LocationRepositoryRouter
import com.example.rickotheque.features.location.data.repository.LocationRestRepositoryImpl
import com.example.rickotheque.features.location.domain.repository.LocationRepository
import com.example.rickotheque.features.location.domain.usecase.GetLocationByIdUseCase
import org.koin.dsl.module

val locationModule =
    module {
        single { LocationApiService(get()) }
        single { LocationGraphqlApiService(get(), get()) }

        single<LocationRepository>(restQualifier) { LocationRestRepositoryImpl(get()) }
        single<LocationRepository>(graphqlQualifier) { LocationGraphqlRepositoryImpl(get()) }
        single<LocationRepository> {
            LocationRepositoryRouter(
                dataSourceSelector = get(),
                restRepository = get(restQualifier),
                graphqlRepository = get(graphqlQualifier),
            )
        }

        single { GetLocationByIdUseCase(get()) }
    }
