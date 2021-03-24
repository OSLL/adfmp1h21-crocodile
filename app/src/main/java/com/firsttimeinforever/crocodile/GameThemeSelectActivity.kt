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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.firsttimeinforever.crocodile.model.GameConfig
import com.google.android.material.internal.ContextUtils.getActivity


class GameThemeSelectActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var recyclerView: RecyclerView

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
            holder.itemView.setOnClickListener {
                if (ApplicationState.config == null) {
                    ApplicationState.config = GameConfig(theme = position)
                } else {
                    ApplicationState.config = ApplicationState.config!!.copy(theme = position)
                }
                startNextActivity()
            }
        }
    }
}
