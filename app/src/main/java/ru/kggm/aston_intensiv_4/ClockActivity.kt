package ru.kggm.aston_intensiv_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kggm.aston_intensiv_4.databinding.ActivityClockBinding

class ClockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}