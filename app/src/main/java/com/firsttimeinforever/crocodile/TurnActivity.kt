package com.firsttimeinforever.crocodile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils
import com.yuyakaido.android.cardstackview.*
import java.util.*

class TurnActivity : AppCompatActivity(), CardStackListener {
    private lateinit var cardStackView: CardStackView
    private lateinit var layoutManager: CardStackLayoutManager
    private lateinit var endTurnButton: Button
    private lateinit var turnTimer: TextView

    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turn)
        cardStackView = findViewById(R.id.turn_card_stack_view)
        endTurnButton = findViewById(R.id.turn_end_turn_button)
        turnTimer = findViewById(R.id.turn_timer)
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
        startTimer(10)
    }

    private fun startTimer(seconds: Int) {
        turnTimer.text = Utility.formatSeconds(seconds)
        var secondsLeft = seconds
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (secondsLeft == 0) {
                    timer.cancel()
                    runOnUiThread {
                        timerExpired()
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

    private fun timerExpired() {
        val dialog = TurnEndedDialogFragment()
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

    class TurnEndedDialogFragment: DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                return with(AlertDialog.Builder(it)) {
                    setMessage("No more time!")
                    setPositiveButton("Ok") { _, _ ->
                        it.finish()
                    }
                    setCancelable(false)
                    create().also { it.setCanceledOnTouchOutside(false) }
                }
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    private class MyRecyclerViewAdapter: RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view  = LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_word_card,
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = 6

        @SuppressLint("RestrictedApi")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // holder.nameTextView.text = names[position]
            // holder.descriptionTextView.text = descriptions[position]
            // if (position == 0) {
            //     holder.crownImageView.visibility = ImageView.VISIBLE
            // }
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }
}
