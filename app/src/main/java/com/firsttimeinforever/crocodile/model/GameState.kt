package com.firsttimeinforever.crocodile.model

import com.firsttimeinforever.crocodile.ApplicationConfig

data class GameState(
    var scores: List<TeamScoreEntry>,
    var turn: Int = ApplicationConfig.TURNS_COUNT,
    var teamForCurrentTurn: Int = scores.size
) {
    companion object {
        fun from(teams: Iterable<Team>): GameState {
            return GameState(teams.map { TeamScoreEntry(it) })
        }
    }

    data class TeamScoreEntry(
        val team: Team,
        var score: Int = 0
    )
}
