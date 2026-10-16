package com.example.rickotheque.features.episode.ui.di

import com.example.rickotheque.features.episode.ui.EpisodeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val episodeUiModule =
    module {
        viewModel { EpisodeViewModel(getEpisodeListUseCase = get()) }
    }
