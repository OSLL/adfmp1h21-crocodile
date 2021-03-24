package com.firsttimeinforever.crocodile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView

class MidTurnActivity : AppCompatActivity() {
    private lateinit var startTurnButton: Button
    private lateinit var stopGameButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var turnTextView: TextView
    private lateinit var nextTeamTextView: TextView
    private lateinit var headerTextView: TextView

    private var gameEnded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mid_turn)
        startTurnButton = findViewById(R.id.mid_turn_start_turn_button)
        stopGameButton = findViewById(R.id.mid_turn_stop_game_button)
        recyclerView = findViewById(R.id.mid_turn_recycler_view)
        turnTextView = findViewById(R.id.mid_turn_turn_number)
        nextTeamTextView = findViewById(R.id.mid_turn_next_team)
        headerTextView = findViewById(R.id.mid_game_header)
        startTurnButton.setOnClickListener {
            startActivityForResult(Intent(this, TurnActivity::class.java), 1)
        }
        stopGameButton.setOnClickListener {
            if (!gameEnded) {
                possiblyStopGame()
            } else {
                val intent = Intent(this, MainMenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }
        updateFromState()
        recyclerView.adapter = MyRecyclerViewAdapter()
        turnTextView.text = "Turn: ${ApplicationState.state!!.turn}"
    }

    private fun possiblyStopGame() {
        val dialog = GameCloseConfirmationDialogFragment()
        dialog.show(supportFragmentManager, null)
    }

    override fun onBackPressed() {
        possiblyStopGame()
    }

    private fun updateFromState() {
        val state = ApplicationState.state!!
        state.scores = state.scores.sortedByDescending { it.score }
        state.teamForCurrentTurn -= 1
        if (state.teamForCurrentTurn < 0) {
            state.turn -= 1
            if (state.turn == 0) {
                setGameEndedState()
            }
            state.teamForCurrentTurn = state.scores.size - 1
        }
        turnTextView.text = state.turn.toString()
        nextTeamTextView.text = ApplicationState.config!!.teams[state.teamForCurrentTurn].name
    }

    private fun setGameEndedState() {
        gameEnded = true
        startTurnButton.visibility = Button.INVISIBLE
        startTurnButton.isEnabled = false
        nextTeamTextView.visibility = TextView.INVISIBLE
        turnTextView.visibility = TextView.INVISIBLE
        headerTextView.text = "Final Scores"
        stopGameButton.text = "Return to Main Menu"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateFromState()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    class GameCloseConfirmationDialogFragment: DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                return with(AlertDialog.Builder(it)) {
                    setMessage("Dou you really want to stop current game?")
                    setPositiveButton("Yes") { _, _ ->
                        val intent = Intent(
                            it,
                            MainMenuActivity::class.java
                        )
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        it.finish()
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                }
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    private class MyRecyclerViewAdapter: RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.team_score_fragment_color)
            val nameTextView: TextView = itemView.findViewById(R.id.team_score_fragment_name)
            val scoreTextView: TextView = itemView.findViewById(R.id.team_score_fragment_score)
            val crownImageView: ImageView = itemView.findViewById(R.id.team_score_fragment_winner_crown)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view  = LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_team_score,
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = ApplicationState.state!!.scores.size

        @SuppressLint("RestrictedApi")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val maxPoints = ApplicationConfig.TURNS_COUNT * ApplicationConfig.WORDS_COUNT
            val (team, score) = ApplicationState.state!!.scores[position]
            holder.imageView.setBackgroundColor(team.color.toArgb())
            holder.nameTextView.text = team.name
            holder.scoreTextView.text = "$score/$maxPoints"
            if ((position == 0 || position != 0 && ApplicationState.state!!.scores[position - 1].score == score) && score != 0) {
                holder.crownImageView.visibility = ImageView.VISIBLE
            }
        }
    }
}
