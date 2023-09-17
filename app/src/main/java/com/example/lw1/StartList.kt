package com.example.lw1

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lw1.databinding.ActivityStartListBinding
import kotlinx.coroutines.delay


class StartList : AppCompatActivity() {

    private lateinit var binding: ActivityStartListBinding
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
            Toast.makeText(this, "Fab3", Toast.LENGTH_SHORT).show()
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
}