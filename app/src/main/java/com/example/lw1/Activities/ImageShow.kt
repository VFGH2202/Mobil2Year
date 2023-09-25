package com.example.lw1.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import com.example.lw1.Item
import com.example.lw1.MainDb
import com.example.lw1.R
import com.example.lw1.databinding.ActivityCameraRedactBinding
import com.example.lw1.databinding.ActivityImageShowBinding
import java.text.SimpleDateFormat
import java.util.*

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

    @Override
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val infl = menuInflater
        infl.inflate(R.menu.image_show_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mn_delete_image){
            Thread{ db.getDao().deleteIt(it) }.start()
        }
        return super.onOptionsItemSelected(item)
    }
}