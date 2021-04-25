package com.firsttimeinforever.crocodile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.firsttimeinforever.crocodile.model.GameConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class GameThemeSelectActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var recyclerView: RecyclerView

    private var state = ApplicationState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_theme_select)
        backButton = findViewById(R.id.game_theme_select_back_button)
        recyclerView = findViewById(R.id.game_theme_select_recycler_view)
        backButton.setOnClickListener {
            finish()
        }
        recyclerView.adapter = MyRecyclerViewAdapter()
    }

    private fun startNextActivity() {
        val intent = Intent(this, GameSettingsActivity::class.java)
        intent.putExtra("state", Json.encodeToString(state))
        startActivity(intent)
    }

    private inner class MyRecyclerViewAdapter: RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.game_theme_card_image)
            val nameTextView: TextView = itemView.findViewById(R.id.game_theme_card_name)
            val descriptionTextView: TextView = itemView.findViewById(R.id.game_theme_card_description)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view  = LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_game_theme_select_card,
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = ApplicationConfig.THEMES_COUNT

        @SuppressLint("RestrictedApi")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val names = holder.itemView.resources.getStringArray(R.array.game_theme_names_array)
            val descriptions = holder.itemView.resources.getStringArray(R.array.game_theme_descriptions_array)
            holder.nameTextView.text = names[position]
            holder.descriptionTextView.text = descriptions[position]
            holder.imageView.setImageResource(holder.itemView.resources.obtainTypedArray(R.array.game_theme_images_array).getResourceId(position, 0))
            holder.itemView.setOnClickListener {
                state = state.copy(config = state.config.copy(theme = position))
                startNextActivity()
            }
        }
    }
}
