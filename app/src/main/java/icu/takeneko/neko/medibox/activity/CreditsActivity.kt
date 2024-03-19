package icu.takeneko.neko.medibox.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import icu.takeneko.neko.medibox.data.CreditItem
import icu.takeneko.neko.medibox.databinding.ActivityCreditsBinding
import icu.takeneko.neko.medibox.view.CreditView

class CreditsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreditsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditsBinding.inflate(layoutInflater)
        setContentView(binding.main)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            this.finish()
        }
        val credits = CreditItem.readCredits(this)
        credits.forEach {
            val view = CreditView(this).apply { creditItem = it }
            binding.listCreditItems.addView(view)
        }

    }
}