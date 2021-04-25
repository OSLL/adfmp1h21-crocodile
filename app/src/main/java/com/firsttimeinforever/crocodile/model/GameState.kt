package com.firsttimeinforever.crocodile.model

import com.firsttimeinforever.crocodile.ApplicationConfig
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    var scores: List<TeamScoreEntry> = emptyList(),
    var turn: Int = ApplicationConfig.TURNS_COUNT,
    var teamForCurrentTurn: Int = scores.size
) {
    companion object {
        fun from(teams: Iterable<Team>): GameState {
            return GameState(teams.map { TeamScoreEntry(it) })
        }
    }

    @Serializable
    data class TeamScoreEntry(
        val team: Team = Team(),
        var score: Int = 0
    )
}
