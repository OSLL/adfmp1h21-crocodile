package com.firsttimeinforever.crocodile.model

import kotlinx.serialization.Serializable

@Serializable
data class GameConfig(
    val theme: Int = 0,
    val teams: MutableList<Team> = mutableListOf(),
    val time: Int = 0
)
