package com.example.lw1.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.lw1.Item
import com.example.lw1.MainDb
import com.example.lw1.R
import com.example.lw1.databinding.ActivityTextShowBinding
import java.text.SimpleDateFormat
import java.util.*

class TextShow : AppCompatActivity() {
    private lateinit var binding: ActivityTextShowBinding
    //Dbase
    private lateinit var db: MainDb
    private lateinit var it: Item
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTextShowBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = MainDb.getDb(this)
        val id = intent.extras?.getInt("ID")
        Thread {
            it = db.getDao().getById(id).apply {
                binding.tvShowHd.setText(this.head)
                binding.tvDat.setText(this.data)
            }
        }.start()
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val infl = menuInflater
        infl.inflate(R.menu.show_menu, menu)
        return true
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mn_delete -> Thread{ db.getDao().deleteIt(it) }.start()
            R.id.mn_upd -> {
                it.head = binding.tvShowHd.text.toString()
                it.data = binding.tvDat.text.toString()
                val formatedDate = SimpleDateFormat("dd-MM-yyyy").format(Date())
                val formatedTime = SimpleDateFormat("HH:mm").format(Date())
                val DateTime = "$formatedDate $formatedTime"
                it.datetime = DateTime
                Thread{ db.getDao().updateIt(it) }.start()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}