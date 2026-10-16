package com.example.rickotheque.features.episode.data.di

import com.example.rickotheque.core.datasource.di.graphqlQualifier
import com.example.rickotheque.core.datasource.di.restQualifier
import com.example.rickotheque.features.episode.data.network.EpisodeApiService
import com.example.rickotheque.features.episode.data.network.EpisodeGraphqlApiService
import com.example.rickotheque.features.episode.data.repository.EpisodeGraphqlRepositoryImpl
import com.example.rickotheque.features.episode.data.repository.EpisodeRepositoryRouter
import com.example.rickotheque.features.episode.data.repository.EpisodeRestRepositoryImpl
import com.example.rickotheque.features.episode.domain.repository.EpisodeRepository
import com.example.rickotheque.features.episode.domain.usecase.GetEpisodesListUseCase
import org.koin.dsl.module

val episodeModule =
    module {
        single { EpisodeApiService(get()) }
        single { EpisodeGraphqlApiService(get()) }

        single<EpisodeRepository>(restQualifier) { EpisodeRestRepositoryImpl(get()) }
        single<EpisodeRepository>(graphqlQualifier) { EpisodeGraphqlRepositoryImpl(get()) }
        single<EpisodeRepository> {
            EpisodeRepositoryRouter(
                dataSourceSelector = get(),
                restRepository = get(restQualifier),
                graphqlRepository = get(graphqlQualifier),
            )
        }

        single { GetEpisodesListUseCase(get()) }
    }
