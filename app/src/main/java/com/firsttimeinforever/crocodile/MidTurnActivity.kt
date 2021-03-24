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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mid_turn)
        startTurnButton = findViewById(R.id.mid_turn_start_turn_button)
        stopGameButton = findViewById(R.id.mid_turn_stop_game_button)
        recyclerView = findViewById(R.id.mid_turn_recycler_view)
        startTurnButton.setOnClickListener {
            startActivity(Intent(this, TurnActivity::class.java))
        }
        stopGameButton.setOnClickListener {
            possiblyStopGame()
        }
        recyclerView.adapter = MyRecyclerViewAdapter()
    }

    private fun possiblyStopGame() {
        val dialog = GameCloseConfirmationDialogFragment()
        dialog.show(supportFragmentManager, null)
    }

    override fun onBackPressed() {
        possiblyStopGame()
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

        override fun getItemCount(): Int = 2

        @SuppressLint("RestrictedApi")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // holder.nameTextView.text = names[position]
            // holder.descriptionTextView.text = descriptions[position]
            if (position == 0) {
                holder.crownImageView.visibility = ImageView.VISIBLE
            }
        }
    }
}
