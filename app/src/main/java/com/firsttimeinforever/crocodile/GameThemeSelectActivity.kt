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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity


class GameThemeSelectActivity : AppCompatActivity() {
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_theme_select)
        nextButton = findViewById(R.id.game_theme_select_next_button)
        backButton = findViewById(R.id.game_theme_select_back_button)
        recyclerView = findViewById(R.id.game_theme_select_recycler_view)

        nextButton.setOnClickListener {
            startActivity(Intent(this, GameSettingsActivity::class.java))
        }
        backButton.setOnClickListener {
            finish()
        }
        recyclerView.adapter = MyRecyclerViewAdapter()
    }

    private class MyRecyclerViewAdapter: RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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
                Toast.makeText(getActivity(holder.itemView.context), "Item $position is clicked.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
