package com.example.lw1.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lw1.Item
import com.example.lw1.MainDb
import com.example.lw1.databinding.ActivityTextRedactBinding
import java.text.SimpleDateFormat
import java.util.*

class TextRedact : AppCompatActivity() {

    private lateinit var binding: ActivityTextRedactBinding
    lateinit var typ: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTextRedactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when(intent.getIntExtra("tp", 0)){
            1->typ = "TXT"
            2->typ = "URL"
        }

        val db = MainDb.getDb(this)
        binding.btAdd.setOnClickListener {
            val formatedDate = SimpleDateFormat("dd-MM-yyyy").format(Date())
            val formatedTime = SimpleDateFormat("HH:mm").format(Date())
            val DateTime = "$formatedDate $formatedTime"

            val item = Item(null,
                binding.tvHead.text.toString(),
                typ,
                binding.tvDat.text.toString(),
                DateTime
            )
            Thread{
                db.getDao().insertItem(item)
            }.start()
        }
    }
}