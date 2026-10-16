package com.example.rickotheque.features.location.ui.di

import com.example.rickotheque.features.location.ui.LocationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val locationUiModule =
    module {
        viewModel { LocationViewModel(getLocationByIdUseCase = get()) }
    }
