package com.firsttimeinforever.crocodile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class GameRulesViewPagerFragment(private val position: Int) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_game_rules_view_pager,
            container,
            false
        )
        val textView = view.findViewById<TextView>(R.id.game_rules_pager_fragment_text_view)
        textView.text = resources.getStringArray(R.array.game_rules_fragment_text_array)[position]
        return view
    }
}
