package com.example.lw1.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.example.lw1.Item
import com.example.lw1.MainDb
import com.example.lw1.databinding.ActivityCameraRedactBinding
import com.example.lw1.databinding.ActivityImageShowBinding

class ImageShow : AppCompatActivity() {
    private lateinit var binding: ActivityImageShowBinding
    private lateinit var db: MainDb
    private lateinit var it: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MainDb.getDb(this)
        val id = intent.extras?.getInt("ID")
        Thread {
            it = db.getDao().getById(id).apply {
                binding.tvShowHd.setText(this.head)
                binding.ivShow.setImageURI(this.data.toUri())
            }
        }.start()
    }
}