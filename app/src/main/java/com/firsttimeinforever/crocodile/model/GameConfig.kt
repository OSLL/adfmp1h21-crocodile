package com.firsttimeinforever.crocodile.model

data class GameConfig(
    val theme: Int = 0,
    val teams: MutableList<Team> = mutableListOf(),
    val time: Int = 0
)
