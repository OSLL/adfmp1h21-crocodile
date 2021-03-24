package com.firsttimeinforever.crocodile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
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
import com.yuyakaido.android.cardstackview.*
import java.util.*

class TurnActivity : AppCompatActivity(), CardStackListener {
    private lateinit var cardStackView: CardStackView
    private lateinit var layoutManager: CardStackLayoutManager
    private lateinit var endTurnButton: Button
    private lateinit var turnTeamName: TextView
    private lateinit var turnTimer: TextView
    private lateinit var turnWordsLeft: TextView
    private lateinit var turnWordsGuessed: TextView

    private val timer = Timer()
    private lateinit var words: List<String>

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
        turnTeamName.text = ApplicationState.config!!.teams[ApplicationState.state!!.teamForCurrentTurn].name
        startTimer(10)
    }

    private fun wordGuessed() {
        val team = ApplicationState.config!!.teams[ApplicationState.state!!.teamForCurrentTurn]
        ApplicationState.state!!.scores.first { it.team == team }.score += 1
        wordsGuessed += 1
        turnWordsGuessed.text = "Guessed: $wordsGuessed/${ApplicationConfig.WORDS_COUNT}"
    }

    @ExperimentalStdlibApi
    private fun getWords(): List<String> {
        val words = resources.getStringArray(R.array.theme_words)[ApplicationState.config!!.theme].split(',')
        return buildList {
            repeat(ApplicationConfig.WORDS_COUNT) {
                add(words.random())
            }
        }
    }

    private fun startTimer(seconds: Int) {
        turnTimer.text = Utility.formatSeconds(seconds)
        var secondsLeft = seconds
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (secondsLeft == 0) {
                    timer.cancel()
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
        val dialog = TurnEndedDialogFragment(timeEnd)
        dialog.show(supportFragmentManager, null)
    }

    private fun possiblyStopTurn() {
        val dialog = TurnCloseConfirmationDialogFragment()
        dialog.show(supportFragmentManager, null)
        timer.cancel()
    }

    override fun onBackPressed() {
        possiblyStopTurn()
    }

    class TurnCloseConfirmationDialogFragment: DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                return with(AlertDialog.Builder(it)) {
                    setMessage("Dou you really want to stop current turn?")
                    setPositiveButton("Yes") { _, _ ->
                        it.setResult(Activity.RESULT_OK, null)
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

    class TurnEndedDialogFragment(private val timeEnd: Boolean = true): DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                return with(AlertDialog.Builder(it)) {
                    if (timeEnd) {
                        setMessage("No more time!")
                    } else {
                        setMessage("Turn ended!")
                    }
                    setPositiveButton("Ok") { _, _ ->
                        it.setResult(Activity.RESULT_OK, null)
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
