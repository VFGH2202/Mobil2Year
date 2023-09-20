package com.example.lw1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lw1.databinding.ActivityTextRedactBinding
import java.text.SimpleDateFormat
import java.util.*

class TextRedact : AppCompatActivity() {

    private lateinit var binding: ActivityTextRedactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTextRedactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = MainDb.getDb(this)
        binding.btAdd.setOnClickListener {
            val formatedDate = SimpleDateFormat("dd-MM-yyyy").format(Date())
            val formatedTime = SimpleDateFormat("HH:mm").format(Date())
            val DateTime = "$formatedDate $formatedTime"

            val item = Item(null,
                binding.tvHead.text.toString(),
            "TXT",
                binding.tvDat.text.toString(),
                DateTime
            )
            Thread{
                db.getDao().insertItem(item)
            }.start()
        }
    }
}