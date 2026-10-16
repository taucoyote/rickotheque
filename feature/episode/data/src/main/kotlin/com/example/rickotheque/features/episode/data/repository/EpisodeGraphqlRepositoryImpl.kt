package com.example.rickotheque.features.episode.data.repository

import com.example.rickotheque.features.episode.data.network.EpisodeGraphqlApiService
import com.example.rickotheque.features.episode.domain.model.EpisodeResponse
import com.example.rickotheque.features.episode.domain.repository.EpisodeRepository

class EpisodeGraphqlRepositoryImpl(
    private val episodeGraphqlApiService: EpisodeGraphqlApiService,
) : EpisodeRepository {
    override suspend fun getEpisodeList(page: Int): EpisodeResponse {
        return episodeGraphqlApiService.getEpisodeList(page)
    }
}
