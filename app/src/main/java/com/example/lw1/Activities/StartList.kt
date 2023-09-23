package com.example.lw1.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lw1.List.ItemList
import com.example.lw1.List.NoteAdapter
import com.example.lw1.MainDb
import com.example.lw1.R
import com.example.lw1.databinding.ActivityStartListBinding


class StartList : AppCompatActivity(), NoteAdapter.Listener {

    private lateinit var binding: ActivityStartListBinding
    private val adapter = NoteAdapter(this)
    //Dbase
    private lateinit var db: MainDb
    private lateinit var type: String


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

        binding.FABase.bringToFront()



        binding.FABase.setOnClickListener {
            if(FABstatus) {
                shrinkFab()
            }else{
                expandFab()
            }
        }
        binding.FABweb.setOnClickListener {
            val intent = Intent(this, TextRedact::class.java)
            intent.putExtra("tp", 2)
            startActivity(intent)
        }

        binding.FABcam.setOnClickListener {
            Toast.makeText(this, "Fab2", Toast.LENGTH_SHORT).show()
        }
        binding.FABtxt.setOnClickListener {
            val intent = Intent(this, TextRedact::class.java)
            intent.putExtra("tp", 1)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    private fun shrinkFab(){
        binding.FABase.startAnimation(RotAntiAddAnim)

        binding.FABweb.startAnimation(Fab1HideAnim)
        binding.FABweb.isClickable = false

        binding.FABcam.startAnimation(Fab2HideAnim)
        binding.FABcam.isClickable = false

        binding.FABtxt.startAnimation(Fab3HideAnim)
        binding.FABtxt.isClickable = false

        FABstatus = !FABstatus
    }

    private fun expandFab(){
        binding.FABase.startAnimation(RotAddAnim)

        binding.FABweb.startAnimation(Fab1ShowAnim)
        binding.FABweb.isClickable = true


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
                adapter.clearList()
                it.forEach {
                    val head = it.head
                    val dt = it.datetime
                    val Id = it.id
                    var img: Int
                    val tp = it.type
                    when(it.type){
                        "TXT"-> img = R.drawable.baseline_text_snippet_24
                        "IMG"-> img = R.drawable.baseline_photo_camera_24
                        "URL"-> img = R.drawable.web_url
                        else -> img = R.drawable.fab_add_icon
                    }

                    val item = ItemList(head, dt, tp, img, Id)
                    adapter.addNote(item)
                }
            }

        }
    }

    override fun onClick(note: ItemList) {
        Toast.makeText(this, "${note.type}", Toast.LENGTH_SHORT).show()
        if (note.type == "TXT"){
            val intent = Intent(this, TextShow::class.java)
            intent.putExtra("ID", note.id)
            startActivity(intent)
        }
        else if (note.type == "URL"){
            UrlDialod(note.id)
        }
    }
    fun UrlDialod(id: Int?) {
        var builder = AlertDialog.Builder(this)
            .setTitle(R.string.URL_Dialog_ttl)
            .setMessage(R.string.URL_Dialog_msg)
            .setPositiveButton("Открыть в браузере") { _, _ ->
                OpenURL(id)
            }
            .setNegativeButton("Просмотреть") { _, _ ->
                val intent = Intent(this, TextShow::class.java)
                intent.putExtra("ID", id)
                startActivity(intent)
            }
            .create()
        builder.show()
    }

    fun OpenURL(id: Int?){
        Thread {
            db.getDao().getById(id).apply {
                val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(this.data))
                startActivity(urlIntent)
            }
        }.start()
    }
}
