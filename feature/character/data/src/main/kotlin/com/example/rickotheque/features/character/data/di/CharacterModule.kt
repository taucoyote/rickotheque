package com.example.rickotheque.features.character.data.di

import com.example.rickotheque.core.datasource.di.graphqlQualifier
import com.example.rickotheque.core.datasource.di.restQualifier
import com.example.rickotheque.features.character.data.cache.CharacterGraphqlCache
import com.example.rickotheque.features.character.data.network.CharacterApiService
import com.example.rickotheque.features.character.data.network.CharacterGraphqlApiService
import com.example.rickotheque.features.character.data.repository.CharacterGraphqlRepositoryImpl
import com.example.rickotheque.features.character.data.repository.CharacterRepositoryRouter
import com.example.rickotheque.features.character.data.repository.CharacterRestRepositoryImpl
import com.example.rickotheque.features.character.domain.repository.CharacterRepository
import com.example.rickotheque.features.character.domain.usecase.GetCharactersUsecase
import org.koin.dsl.module

val characterModule =
    module {
        single { CharacterGraphqlCache() }

        single { CharacterApiService(get()) }
        single { CharacterGraphqlApiService(get(), get()) }

        single<CharacterRepository>(restQualifier) { CharacterRestRepositoryImpl(get()) }
        single<CharacterRepository>(graphqlQualifier) { CharacterGraphqlRepositoryImpl(get()) }
        single<CharacterRepository> {
            CharacterRepositoryRouter(
                dataSourceSelector = get(),
                restRepository = get(restQualifier),
                graphqlRepository = get(graphqlQualifier),
            )
        }

        single { GetCharactersUsecase(get()) }
    }
