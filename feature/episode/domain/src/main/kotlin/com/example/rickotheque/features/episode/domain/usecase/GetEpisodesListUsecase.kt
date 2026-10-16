package com.example.rickotheque.features.episode.domain.usecase

import com.example.rickotheque.features.episode.domain.model.EpisodeResponse
import com.example.rickotheque.features.episode.domain.repository.EpisodeRepository

class GetEpisodesListUseCase(private val repository: EpisodeRepository) {
    suspend operator fun invoke(page: Int): EpisodeResponse = repository.getEpisodeList(page)
}
