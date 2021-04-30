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
import com.firsttimeinforever.crocodile.model.GameState
import com.firsttimeinforever.crocodile.model.Teams
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameSettingsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addTeamButton: Button
    private lateinit var startGameButton: Button
    private lateinit var backButton: Button
    private lateinit var turnTimeSeekbar: SeekBar
    private lateinit var turnTimeTextView: TextView

    private lateinit var state: ApplicationState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)
        recyclerView = findViewById(R.id.game_settings_teams_recycler_view)
        addTeamButton = findViewById(R.id.game_settings_add_team_button)
        startGameButton = findViewById(R.id.game_settings_start_game_button)
        backButton = findViewById(R.id.game_settings_back_button)
        turnTimeSeekbar = findViewById(R.id.game_setting_turn_time_seekbar)
        turnTimeTextView = findViewById(R.id.game_settings_turn_time)
        // with(ApplicationState.config!!) {
        //     teams.clear()
        //     teams.add(Teams.PREDEFINED[0])
        //     teams.add(Teams.PREDEFINED[1])
        // }

        state = Json.decodeFromString(intent.getStringExtra("state"))
        state.config.teams.clear()
        state.config.teams.add(Teams.PREDEFINED[0])
        state.config.teams.add(Teams.PREDEFINED[1])

        val recyclerViewAdapter = MyRecyclerViewAdapter(recyclerView)
        recyclerView.adapter = recyclerViewAdapter
        addTeamButton.setOnClickListener {
            recyclerViewAdapter.addTeam()
        }
        startGameButton.setOnClickListener {
            state = state.copy(game = GameState.from(state.config.teams))
            // ApplicationState.state = GameState.from(ApplicationState.config!!.teams)
            val intent = Intent(this, MidTurnActivity::class.java)
            intent.putExtra("state", Json.encodeToString(state))
            startActivity(intent)
        }
        backButton.setOnClickListener {
            finish()
        }
        turnTimeSeekbar.progress = 40
        updateTurnTime(turnTimeSeekbar.progress)
        turnTimeSeekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, position: Int, fromUser: Boolean) {
                updateTurnTime(position)
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekbar: SeekBar?) = Unit
        })
    }

    private fun updateTurnTime(value: Int) {
        state = state.copy(config = state.config.copy(time = value))
        // ApplicationState.config = ApplicationState.config!!.copy(time = value)
        turnTimeTextView.text = formatSeconds(value)
    }

    private inner class MyRecyclerViewAdapter(val recyclerView: RecyclerView): RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
        @SuppressLint("RestrictedApi")
        fun addTeam() {
            // val count = ApplicationState.config!!.teams.size
            val count = state.config.teams.size
            if (count == ApplicationConfig.MAX_TEAMS_COUNT) {
                Toast.makeText(
                    getActivity(recyclerView.context),
                    "Maximum ${ApplicationConfig.MAX_TEAMS_COUNT} of teams can be declared!",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            val next = Teams.PREDEFINED.find { it !in state.config.teams }
            requireNotNull(next)
            // ApplicationState.config!!.teams.add(next)
            state.config.teams.add(next)
            notifyDataSetChanged()
            Toast.makeText(getActivity(recyclerView.context), "${next.name} added!", Toast.LENGTH_SHORT).show()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view  = LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_team,
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = state.config.teams.size

        @SuppressLint("RestrictedApi")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.colorImageView.setBackgroundColor(state.config.teams[position].color.toArgb())
            holder.teamNameTextView.text = state.config.teams[position].name
            holder.itemView.setOnClickListener {
                Toast.makeText(getActivity(holder.itemView.context), "Item $position is clicked.", Toast.LENGTH_SHORT).show()
            }
        }

        @SuppressLint("RestrictedApi")
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val colorImageView: ImageView = itemView.findViewById(R.id.team_fragment_color)
            val teamNameTextView: TextView = itemView.findViewById(R.id.team_fragment_name)
            val removeButton: ImageButton = itemView.findViewById(R.id.team_fragment_remove_button)

            init {
                removeButton.setOnClickListener {
                    val count = state.config.teams.size
                    if (count == 2) {
                        Toast.makeText(getActivity(itemView.context), "At least two teams should be declared!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    state.config.teams.removeAt(layoutPosition)
                    notifyDataSetChanged()
                }
            }
        }
    }
}
