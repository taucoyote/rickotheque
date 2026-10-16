package com.example.rickotheque

import android.app.Application
import com.example.rickotheque.core.datasource.di.dataSourceModule
import com.example.rickotheque.core.network.di.coreNetworkModule
import com.example.rickotheque.core.network.graphql.di.coreNetworkGraphqlModule
import com.example.rickotheque.features.character.data.di.characterModule
import com.example.rickotheque.features.character.ui.di.characterUiModule
import com.example.rickotheque.features.episode.data.di.episodeModule
import com.example.rickotheque.features.episode.ui.di.episodeUiModule
import com.example.rickotheque.features.location.data.di.locationModule
import com.example.rickotheque.features.location.ui.di.locationUiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RickApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RickApplication)
            modules(
                dataSourceModule,
                coreNetworkModule,
                coreNetworkGraphqlModule,
                characterModule,
                characterUiModule,
                episodeModule,
                episodeUiModule,
                locationModule,
                locationUiModule,
            )
        }
    }
}
