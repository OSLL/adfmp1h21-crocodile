package com.firsttimeinforever.crocodile

object Utility {
    fun formatSeconds(value: Int, appendUnits: Boolean = false): String {
        val minutes = value / 60
        val seconds = value - minutes * 60
        return buildString {
            when (minutes) {
                in 0..9 -> append("0$minutes")
                else -> append(minutes)
            }
            append(':')
            when (seconds) {
                in 0..9 -> append("0$seconds")
                else -> append(seconds)
            }
            if (appendUnits) {
                append(
                    when (minutes) {
                        0 -> " seconds"
                        else -> " minutes"
                    }
                )
            }
        }
    }
}
