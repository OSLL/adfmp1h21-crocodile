package com.firsttimeinforever.crocodile

import com.firsttimeinforever.crocodile.model.GameConfig
import com.firsttimeinforever.crocodile.model.GameState
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ApplicationState(
    var config: GameConfig = GameConfig(),
    var game: GameState = GameState()
)
