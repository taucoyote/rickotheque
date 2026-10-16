package com.example.rickotheque.features.character.ui.di

import com.example.rickotheque.features.character.ui.CharacterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val characterUiModule =
    module {
        viewModel { CharacterViewModel(getCharactersUsecase = get()) }
    }
