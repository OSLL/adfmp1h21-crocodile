package com.firsttimeinforever.crocodile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GameRulesActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var backButton: Button
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_rules)
        viewPager = findViewById(R.id.game_rules_view_pager)
        backButton = findViewById(R.id.game_rules_back_button)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager.adapter = PagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position -> }.attach()
        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    private inner class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return resources.getStringArray(R.array.game_rules_fragment_text_array).size
        }

        override fun createFragment(position: Int): Fragment {
            return GameRulesViewPagerFragment(position)
        }
    }
}
