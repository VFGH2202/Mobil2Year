package com.example.lw1

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lw1.List.ItemList
import com.example.lw1.List.NoteAdapter
import com.example.lw1.databinding.ActivityStartListBinding


class StartList : AppCompatActivity(), NoteAdapter.Listener {

    private lateinit var binding: ActivityStartListBinding
    private val adapter = NoteAdapter(this)
    //Dbase
    private lateinit var db: MainDb


    private var FABstatus = false


    private val Fab1ShowAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fab1_show)
    }
    private val Fab2ShowAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fab2_show)
    }
    private val Fab3ShowAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fab3_show)
    }
    private val Fab1HideAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fab1_hide)
    }
    private val Fab2HideAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fab2_hide)
    }
    private val Fab3HideAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fab3_hide)
    }
    private val RotAddAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_add_but)
    }
    private val RotAntiAddAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_anti_add_but)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = MainDb.getDb(this)
        init()

        binding.FABase.bringToFront()



        binding.FABase.setOnClickListener {
            if(FABstatus) {
                shrinkFab()
            }else{
                expandFab()
            }
        }
        binding.FABmic.setOnClickListener {
            Toast.makeText(this, "Fab1", Toast.LENGTH_SHORT).show()
        }

        binding.FABcam.setOnClickListener {
            Toast.makeText(this, "Fab2", Toast.LENGTH_SHORT).show()
        }
        binding.FABtxt.setOnClickListener {
            //Toast.makeText(this, "Fab3", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TextRedact::class.java)
            startActivity(intent)
        }
    }

    private fun shrinkFab(){
        binding.FABase.startAnimation(RotAntiAddAnim)

        binding.FABmic.startAnimation(Fab1HideAnim)
        binding.FABmic.isClickable = false

        binding.FABcam.startAnimation(Fab2HideAnim)
        binding.FABcam.isClickable = false

        binding.FABtxt.startAnimation(Fab3HideAnim)
        binding.FABtxt.isClickable = false

        FABstatus = !FABstatus
    }

    private fun expandFab(){
        binding.FABase.startAnimation(RotAddAnim)

        binding.FABmic.startAnimation(Fab1ShowAnim)
        binding.FABmic.isClickable = true


        binding.FABcam.startAnimation(Fab2ShowAnim)
        binding.FABcam.isClickable = true

        binding.FABtxt.startAnimation(Fab3ShowAnim)
        binding.FABtxt.isClickable = true

        FABstatus = !FABstatus
    }

    private fun init(){
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(this@StartList)
            rcView.adapter = adapter

            db.getDao().getAllItems().asLiveData().observe(this@StartList){
                it.forEach {
                    val head = it.head
                    val dt = it.datetime
                    val Id = it.id
                    var tp: Int
                    when(it.type){
                        "TXT"-> tp = R.drawable.baseline_text_snippet_24
                        "IMG"-> tp = R.drawable.baseline_photo_camera_24
                        "URL"-> tp = R.drawable.web_url
                        else -> tp = R.drawable.fab_add_icon
                    }

                    val item = ItemList(head, dt, tp, Id)
                    adapter.addNote(item)
                }
            }

        }
    }

    override fun onClick(note: ItemList) {
        Toast.makeText(this, "${note.id}", Toast.LENGTH_SHORT).show()
    }
}