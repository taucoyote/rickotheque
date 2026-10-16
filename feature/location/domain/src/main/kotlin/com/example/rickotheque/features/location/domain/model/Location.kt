package com.example.rickotheque.features.location.domain.model

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    // Resident characters are only known by URL at this level (e.g.
    // "https://rickandmortyapi.com/api/character/1,2,3"); resolving them to
    // real Characters goes through feature:character's repository, exactly
    // like Episode already does for its own `characters` list.
    val residents: List<String> = emptyList(),
    val url: String? = null,
    val created: String? = null,
)
