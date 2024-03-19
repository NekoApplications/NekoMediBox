package icu.takeneko.neko.medibox.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.databinding.ActivityEditMediBoxBinding

class EditMediBoxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMediBoxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMediBoxBinding.inflate(layoutInflater)
        setContentView(binding.main)
    }
}