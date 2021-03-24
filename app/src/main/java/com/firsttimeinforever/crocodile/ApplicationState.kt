package com.firsttimeinforever.crocodile

import com.firsttimeinforever.crocodile.model.GameConfig
import com.firsttimeinforever.crocodile.model.GameState

internal object ApplicationState {
    var config: GameConfig? = null
    var state: GameState? = null

    fun clear() {
        config = null
        state = null
    }
}
