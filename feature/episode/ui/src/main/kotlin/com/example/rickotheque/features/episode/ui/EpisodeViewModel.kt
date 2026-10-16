package com.example.rickotheque.features.episode.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickotheque.features.episode.domain.model.Episode
import com.example.rickotheque.features.episode.domain.usecase.GetEpisodesListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EpisodeViewModel(
    private val getEpisodeListUseCase: GetEpisodesListUseCase
) : ViewModel() {
    private val _isLoadingList = MutableStateFlow<Boolean>(false)
    val isLoadingList: StateFlow<Boolean> = _isLoadingList

    private val _episodes = MutableStateFlow<List<Episode>>(emptyList())
    val episodes: StateFlow<List<Episode>> = _episodes.asStateFlow()

    private var currentPage = 1
    private var totalPages = 1

    fun fetchEpisodeList() {
        viewModelScope.launch {
            if (_isLoadingList.value) return@launch

            _isLoadingList.value = true
            val episodeResponse = getEpisodeListUseCase(currentPage)

            if (episodeResponse.info.count == _episodes.value.size) {
                return@launch
            }
            if (episodeResponse.results.isNotEmpty()) {
                _episodes.value += episodeResponse.results
                totalPages = episodeResponse.info.pages
            }
            _isLoadingList.value = false
        }
    }

    fun loadNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchEpisodeList()
        }
    }

    /**
     * Loads every page up front (only ~3 pages / 51 episodes for this API),
     * so the UI can group everything by season instead of paginating an
     * infinite scroll.
     */
    fun fetchAllEpisodes() {
        viewModelScope.launch {
            if (_isLoadingList.value || currentPage > totalPages && _episodes.value.isNotEmpty()) return@launch

            _isLoadingList.value = true
            try {
                do {
                    val episodeResponse = getEpisodeListUseCase(currentPage)
                    if (episodeResponse.results.isNotEmpty()) {
                        _episodes.value += episodeResponse.results
                        totalPages = episodeResponse.info.pages
                    }
                    currentPage++
                } while (currentPage <= totalPages)
            } finally {
                _isLoadingList.value = false
            }
        }
    }

    fun getEpisodeById(episodeId: Int): Episode? = _episodes.value.find { it.id == episodeId }
}
