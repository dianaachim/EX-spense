package com.example.ex_spences

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewPager2: ViewPager2
    lateinit var tabLayout: TabLayout
    lateinit var tabLayoutMediator: TabLayoutMediator
    lateinit var badgeDrawable: BadgeDrawable
    lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2 = findViewById(R.id.view_pager)
        viewPager2.adapter = PagerAdapter(this)

        mFirebaseAuth = FirebaseAuth.getInstance()
        exit_button.setOnClickListener{
            mFirebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        tabLayout = findViewById(R.id.tab_layout)
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2,
            TabLayoutMediator.TabConfigurationStrategy { tab, position -> // Styling each tab here
                when (position) {
                    1 -> {
                        tab.text = "Accounts"
                        tab.setIcon(R.drawable.ic_credit_card)
                    }
                    2 -> {
                        tab.text = "Stats."
                        tab.setIcon(R.drawable.ic_bar_chart)
//                        badgeDrawable = tab.orCreateBadge
//                        badgeDrawable.backgroundColor = Color.CYAN
//                        badgeDrawable.setVisible(true)
//                        badgeDrawable.number = 100
//                        badgeDrawable.maxCharacterCount = 3
                    }
                    3 -> {
                        tab.text = "Vault"
                        tab.setIcon(R.drawable.ic_account_balance_wallet)
                    }
                    else -> {
                        tab.text = "Main"
                        tab.setIcon(R.drawable.ic_person)
                    }
                }
            })
        tabLayoutMediator.attach()
    }
}
