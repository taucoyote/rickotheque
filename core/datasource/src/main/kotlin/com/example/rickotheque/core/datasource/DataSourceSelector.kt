package com.example.rickotheque.core.datasource

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Single source of truth for which transport (REST/Ktor or GraphQL/Apollo) is
 * currently active. Set once from the choice screen and read at call time by
 * every feature's repository router, so switching the transport never
 * requires touching the Koin graph.
 */
class DataSourceSelector {
    private val _current = MutableStateFlow(DataSource.REST)
    val current: StateFlow<DataSource> = _current.asStateFlow()

    fun select(dataSource: DataSource) {
        _current.value = dataSource
    }
}
