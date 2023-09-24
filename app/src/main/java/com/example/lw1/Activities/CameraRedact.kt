 package com.example.lw1.Activities

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lw1.Item
import com.example.lw1.MainDb
import com.example.lw1.R
import com.example.lw1.databinding.ActivityCameraRedactBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

 class CameraRedact : AppCompatActivity() {
    // Обмен данными между потоками
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityCameraRedactBinding

    private lateinit var outputDir: File
    // Захват данных с превью
    private var imageCapture: ImageCapture? = null

    private fun getOutputDir(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.absoluteFile.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdir()
            }
        }
        if (mediaDir != null && mediaDir.exists()) return mediaDir
        else return filesDir
    }

    companion object{
        private const val FILE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PERMISSION_CODE = 10
        private val PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
    }

    private fun allPermissionGranted() = PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager
            .PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraRedactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionGranted()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(
                this,
                PERMISSION,
                PERMISSION_CODE
            )
        }

        binding.btCam.setOnClickListener {
            takePhoto()
        }

        outputDir = getOutputDir()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

     override fun onDestroy() {
         super.onDestroy()
         cameraExecutor.shutdown()
     }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (requestCode == PERMISSION_CODE){
            if (allPermissionGranted()){
                startCamera()
            }else{
                Toast.makeText(this, "Ошибка разрешения",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        // Связка GUI и потока
        val cameraProviderFuture = ProcessCameraProvider
            .getInstance(this)
        cameraProviderFuture.addListener(
            Runnable {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                    .also {
                        // Привязка камеры к интерфейсу
                        it.setSurfaceProvider(binding.pvCamera.surfaceProvider)
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                imageCapture = ImageCapture
                    .Builder()
                    .build()

                try {
                    // Проверка что ничто не привязано к камере
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                }catch (e: Exception){
                    Log.e("Camera", "Bind error", e)
                }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val db = MainDb.getDb(this)
        val head = intent.getStringExtra("hd").toString()
        val formatedDate = SimpleDateFormat("dd-MM-yyyy").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
        val DateTime = "$formatedDate $formatedTime"

        val imageCapture = imageCapture?:return

        val photoFile = File(outputDir,
        SimpleDateFormat(FILE_FORMAT, Locale.US)
            .format(System.currentTimeMillis()) + ".jpg")

        val outputOption = ImageCapture.OutputFileOptions
            .Builder(photoFile).build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults:
                                          ImageCapture.OutputFileResults) {
                    val uri = Uri.fromFile(photoFile)
                    val msg = "Фото: $uri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

                    val item = Item(null,
                        head,
                        "IMG",
                        uri.toString(),
                        DateTime
                    )
                    Thread{
                        db.getDao().insertItem(item)
                    }.start()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        baseContext,
                        "Ошибка сохранения ${exception.message}",
                        Toast.LENGTH_LONG).show()
                }

            }
        )
    }
}