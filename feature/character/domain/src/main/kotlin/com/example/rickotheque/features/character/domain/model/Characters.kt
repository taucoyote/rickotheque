package com.example.rickotheque.features.character.domain.model

data class Characters(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String? = null,
    val origin: Origin? = null,
    val location: Location? = null,
    val image: String?,
    val episode: List<String>? = null,
    val url: String? = null,
    val created: String? = null
)

data class Origin(
    val name: String,
    val url: String
)

data class Location(
    val name: String,
    val url: String
)
