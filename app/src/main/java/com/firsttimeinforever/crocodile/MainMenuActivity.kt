package com.firsttimeinforever.crocodile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainMenuActivity : AppCompatActivity() {
    private lateinit var gameRulesButton: Button
    private lateinit var startGameButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        gameRulesButton = findViewById(R.id.game_rules_button)
        startGameButton = findViewById(R.id.new_game_button)

        startGameButton.setOnClickListener {
            ApplicationState.clear()
            startActivity(Intent(this, GameThemeSelectActivity::class.java))
        }
        gameRulesButton.setOnClickListener {
            startActivity(Intent(this, GameRulesActivity::class.java))
        }
    }
}
