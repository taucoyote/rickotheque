package com.example.rickotheque.features.episode.data.repository

import com.example.rickotheque.features.episode.data.network.EpisodeApiService
import com.example.rickotheque.features.episode.domain.model.EpisodeResponse
import com.example.rickotheque.features.episode.domain.repository.EpisodeRepository

class EpisodeRestRepositoryImpl(private val episodeApiService: EpisodeApiService) : EpisodeRepository {
    override suspend fun getEpisodeList(page: Int): EpisodeResponse {
        return episodeApiService.getEpisodeList(page)
    }
}
