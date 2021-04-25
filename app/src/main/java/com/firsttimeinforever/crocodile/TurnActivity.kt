package com.firsttimeinforever.crocodile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.firsttimeinforever.crocodile.model.GameConfig
import com.firsttimeinforever.crocodile.model.GameState
import com.yuyakaido.android.cardstackview.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.util.*

class TurnActivity : AppCompatActivity(), CardStackListener {
    private lateinit var cardStackView: CardStackView
    private lateinit var layoutManager: CardStackLayoutManager
    private lateinit var endTurnButton: Button
    private lateinit var turnTeamName: TextView
    private lateinit var turnTimer: TextView
    private lateinit var turnWordsLeft: TextView
    private lateinit var turnWordsGuessed: TextView

    private lateinit var timer: Timer
    private var secondsLeft = 0
    private var timerIsRunning = false
    private lateinit var words: List<String>

    private lateinit var state: ApplicationState

    private var wordsLeft = ApplicationConfig.WORDS_COUNT
    private var wordsGuessed = 0

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turn)
        cardStackView = findViewById(R.id.turn_card_stack_view)
        endTurnButton = findViewById(R.id.turn_end_turn_button)
        turnTimer = findViewById(R.id.turn_timer)
        turnWordsLeft = findViewById(R.id.turn_words_left)
        turnWordsGuessed = findViewById(R.id.turn_words_guessed)
        turnTeamName = findViewById(R.id.turn_team)

        state = Json.decodeFromString(intent.getStringExtra("state"))

        words = getWords()
        layoutManager = CardStackLayoutManager(this, this).apply {
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
            setStackFrom(StackFrom.Top)
        }
        cardStackView.layoutManager = layoutManager
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
        cardStackView.adapter = MyRecyclerViewAdapter()
        endTurnButton.setOnClickListener {
            possiblyStopTurn()
        }
        turnWordsGuessed.text = "Guessed: $wordsGuessed/${ApplicationConfig.WORDS_COUNT}"
        turnWordsLeft.text = "Words left: 0/${ApplicationConfig.WORDS_COUNT}"
        turnTeamName.text = state.config.teams[state.game.teamForCurrentTurn].name
        startTimer(state.config.time)
    }

    private fun wordGuessed() {
        val team = state.config.teams[state.game.teamForCurrentTurn]
        state.game.scores.first { it.team == team }.score += 1
        wordsGuessed += 1
        turnWordsGuessed.text = "Guessed: $wordsGuessed/${ApplicationConfig.WORDS_COUNT}"
    }

    @ExperimentalStdlibApi
    private fun getWords(): List<String> {
        val words = resources.getStringArray(R.array.theme_words)[state.config.theme].split(',')
        return buildList {
            repeat(ApplicationConfig.WORDS_COUNT) {
                add(words.random())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (timerIsRunning) {
            timer.cancel()
        }
        timerIsRunning = false
    }

    override fun onResume() {
        super.onResume()
        if (!timerIsRunning) {
            startTimer(secondsLeft)
        }
    }

    private fun startTimer(seconds: Int) {
        timerIsRunning = true
        turnTimer.text = Utility.formatSeconds(seconds)
        secondsLeft = seconds
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (secondsLeft == 0) {
                    timer.cancel()
                    timerIsRunning = false
                    runOnUiThread {
                        try {
                            stopTurn()
                        } catch (exception: java.lang.IllegalStateException) {
                            exception.printStackTrace()
                        }
                    }
                } else {
                    runOnUiThread {
                        secondsLeft -= 1
                        turnTimer.text = Utility.formatSeconds(secondsLeft)
                        println(secondsLeft)
                    }
                }
            }
        }, 1.toLong() * 1000, 1.toLong() * 1000)
    }

    private fun stopTurn(timeEnd: Boolean = true) {
        val dialog = TurnEndedDialogFragment(state, timeEnd)
        dialog.show(supportFragmentManager, null)
    }

    private fun possiblyStopTurn() {
        val dialog = TurnCloseConfirmationDialogFragment(state)
        dialog.show(supportFragmentManager, null)
        timer.cancel()
        timerIsRunning = false
    }

    override fun onBackPressed() {
        possiblyStopTurn()
    }

    class TurnCloseConfirmationDialogFragment(private val state: ApplicationState = ApplicationState()): DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                return with(AlertDialog.Builder(it)) {
                    setMessage("Dou you really want to stop current turn?")
                    setPositiveButton("Yes") { _, _ ->
                        val result = Intent()
                        result.putExtra("state", Json.encodeToString(state))
                        it.setResult(Activity.RESULT_OK, result)
                        it.finish()
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setCancelable(false)
                    create().also { it.setCanceledOnTouchOutside(false) }
                }
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    class TurnEndedDialogFragment(private val state: ApplicationState = ApplicationState(), private val timeEnd: Boolean = true): DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                return with(AlertDialog.Builder(it)) {
                    when {
                        timeEnd -> setMessage("No more time!")
                        else -> setMessage("Turn ended!")
                    }
                    setPositiveButton("Ok") { _, _ ->
                        val result = Intent()
                        result.putExtra("state", Json.encodeToString(state))
                        it.setResult(Activity.RESULT_OK, result)
                        it.setResult(Activity.RESULT_OK, result)
                        it.finish()
                    }
                    setCancelable(false)
                    create().also { it.setCanceledOnTouchOutside(false) }
                }
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    private inner class MyRecyclerViewAdapter: RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val wordTextView: TextView = itemView.findViewById(R.id.word_fragment_text)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view  = LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_word_card,
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = words.size

        @SuppressLint("RestrictedApi")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.wordTextView.text = words[position]
        }
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction == Direction.Right) {
            wordGuessed()
        }
        wordsLeft -= 1
        val count = ApplicationConfig.WORDS_COUNT - wordsLeft
        turnWordsLeft.text = "Words left: $count/${ApplicationConfig.WORDS_COUNT}"
        if (wordsLeft == 0) {
            stopTurn(false)
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) = Unit
    override fun onCardDragging(direction: Direction?, ratio: Float) = Unit
    override fun onCardCanceled() = Unit
    override fun onCardAppeared(view: View?, position: Int) = Unit
    override fun onCardRewound() = Unit
}
