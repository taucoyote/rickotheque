package com.example.rickotheque.features.character.data.cache

import com.example.rickotheque.features.character.domain.model.Characters

/**
 * Tiny in-memory cache, deliberately simple (no TTL/eviction — this is a
 * demo app, not production code): lets the GraphQL character fetch skip a
 * network round-trip when another GraphQL call already resolved the same
 * characters in one shot.
 *
 * This is what makes `LocationGraphqlApiService` meaningfully faster than
 * its REST counterpart: the REST `/location/{id}` endpoint can only ever
 * return resident *URLs* (a second `/character/...` call is unavoidable),
 * whereas a GraphQL `LocationById` query can ask for full resident profiles
 * directly and stash them here — so the follow-up
 * `CharacterRepository.getCharacters(...)` call (made by
 * `LocationDetailScreen` to display residents) becomes a pure cache hit
 * instead of a second network call, when GraphQL is the active transport.
 */
class CharacterGraphqlCache {
    private val cache = mutableMapOf<String, Characters>()

    fun putAll(characters: List<Characters>) {
        characters.forEach { cache[it.id.toString()] = it }
    }

    /** Returns the characters for [ids] only if *every* id is already cached, null otherwise. */
    fun getAllOrNull(ids: List<String>): List<Characters>? {
        val found = ids.mapNotNull { cache[it] }
        return found.takeIf { it.size == ids.size }
    }
}
