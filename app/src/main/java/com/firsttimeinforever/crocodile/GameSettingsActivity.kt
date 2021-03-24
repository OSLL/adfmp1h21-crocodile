package com.firsttimeinforever.crocodile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.firsttimeinforever.crocodile.Utility.formatSeconds
import com.google.android.material.internal.ContextUtils.getActivity

class GameSettingsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addTeamButton: Button
    private lateinit var startGameButton: Button
    private lateinit var backButton: Button
    private lateinit var turnTimeSeekbar: SeekBar
    private lateinit var turnTimeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)
        recyclerView = findViewById(R.id.game_settings_teams_recycler_view)
        addTeamButton = findViewById(R.id.game_settings_add_team_button)
        startGameButton = findViewById(R.id.game_settings_start_game_button)
        backButton = findViewById(R.id.game_settings_back_button)
        turnTimeSeekbar = findViewById(R.id.game_setting_turn_time_seekbar)
        turnTimeTextView = findViewById(R.id.game_settings_turn_time)
        val recyclerViewAdapter = MyRecyclerViewAdapter(recyclerView)
        recyclerView.adapter = recyclerViewAdapter
        addTeamButton.setOnClickListener {
            recyclerViewAdapter.addTeam()
        }
        startGameButton.setOnClickListener {
            startActivity(Intent(this, MidTurnActivity::class.java))
        }
        backButton.setOnClickListener {
            finish()
        }
        turnTimeSeekbar.progress = 40
        formatSeconds(turnTimeSeekbar.progress, appendUnits = true)
        turnTimeTextView.text = formatSeconds(turnTimeSeekbar.progress)
        turnTimeSeekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, position: Int, fromUser: Boolean) {
                turnTimeTextView.text = formatSeconds(position, appendUnits = true)
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekbar: SeekBar?) = Unit
        })
    }

    private class MyRecyclerViewAdapter(val recyclerView: RecyclerView): RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
        private var itemsCountHolder = 2

        @SuppressLint("RestrictedApi")
        fun addTeam() {
            if (itemsCountHolder == ApplicationConfig.MAX_TEAMS_COUNT) {
                Toast.makeText(
                    getActivity(recyclerView.context),
                    "Maximum ${ApplicationConfig.MAX_TEAMS_COUNT} of teams can be declared!",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            itemsCountHolder += 1
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view  = LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_team,
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = itemsCountHolder

        @SuppressLint("RestrictedApi")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                Toast.makeText(getActivity(holder.itemView.context), "Item $position is clicked.", Toast.LENGTH_SHORT).show()
            }
        }

        @SuppressLint("RestrictedApi")
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val colorImageView: ImageView = itemView.findViewById(R.id.team_fragment_color)
            val teamNameTextView: TextView = itemView.findViewById(R.id.team_fragment_name)
            val removeButton: Button = itemView.findViewById(R.id.team_fragment_remove_button)

            init {
                removeButton.setOnClickListener {
                    if (itemsCountHolder == 2) {
                        Toast.makeText(getActivity(itemView.context), "At least two teams should be declared!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    itemsCountHolder -= 1
                    recyclerView.removeViewAt(layoutPosition)
                }
            }
        }
    }
}
