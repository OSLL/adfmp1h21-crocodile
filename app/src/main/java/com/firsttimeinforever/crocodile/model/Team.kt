package com.firsttimeinforever.crocodile.model

import android.graphics.Color
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Team(
    @Serializable(with = ColorSerializer::class)
    val color: Color = Color.valueOf(Color.BLACK),
    val name: String = ""
)
