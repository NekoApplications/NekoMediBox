package icu.takeneko.neko.medibox.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.databinding.ActivityDisplayMediBoxBinding
import kotlin.properties.Delegates

class DisplayMediBoxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayMediBoxBinding
    private var isPreview by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isPreview = intent.getBooleanExtra("preview", false)
        binding = ActivityDisplayMediBoxBinding.inflate(layoutInflater)
        setContentView(binding.main)
    }
}