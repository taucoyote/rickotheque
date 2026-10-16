package com.example.rickotheque.features.episode.domain.repository

import com.example.rickotheque.features.episode.domain.model.EpisodeResponse

interface EpisodeRepository {
    suspend fun getEpisodeList(page: Int): EpisodeResponse
}
