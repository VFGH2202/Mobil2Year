package com.example.lw1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lw1.databinding.ActivityTextRedactBinding

class TextRedact : AppCompatActivity() {

    private lateinit var binding: ActivityTextRedactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTextRedactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
    }
}