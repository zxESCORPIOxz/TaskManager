package com.errorcorp.taskmanager.Fragment.qr

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.errorcorp.taskmanager.Model.BarCode
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.MyApplication
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.button.MaterialButton
import com.google.mlkit.vision.barcode.common.Barcode
import java.io.IOException

class qr_opt_scanner : Fragment(), View.OnClickListener {

    private val CAMERA_PERMISSION_REQUEST_CODE = 1000

    //SurfaceView
    private lateinit var surfaceView: SurfaceView

    //MaterialButton
    private lateinit var btnflash: MaterialButton
    private lateinit var btnimagen: MaterialButton
    private lateinit var btnremove: MaterialButton
    private lateinit var btnadd: MaterialButton

    //SeekBar
    private lateinit var seekBar: SeekBar

    //CameraSource
    private lateinit var cameraSource: CameraSource

    private lateinit var barcodescanned: BarCode

    //Detected
    private var detected:Boolean = true
    private var handler:Handler = Handler()

    private val runnable = object : Runnable {
        override fun run() {
            if (!detected) {

            }
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_qr_opt_scanner, container, false)

        handler.postDelayed(runnable, 1000)

        surfaceView = view.findViewById(R.id.cameraView)

        btnflash = view.findViewById(R.id.btnflash)
        btnflash.setOnClickListener(this)
        btnimagen = view.findViewById(R.id.btnimagen)
        btnimagen.setOnClickListener(this)
        btnremove = view.findViewById(R.id.btnremove)
        btnremove.setOnClickListener(this)
        btnadd = view.findViewById(R.id.btnadd)
        btnadd.setOnClickListener(this)

        seekBar = view.findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val camera = getCamera(cameraSource)
                if (camera != null) {
                    try {
                        val param = camera.parameters
                        param.zoom = progress
                        camera.parameters = param
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        if (!areCameraPermissionsGranted()) {
            requestCameraPermissions()
        } else {
            startScanner()
        }

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btnflash) -> {
                val camera = getCamera(cameraSource)
                if (camera != null) {
                    try {
                        val param = camera.parameters
                        if (param.flashMode == Camera.Parameters.FLASH_MODE_OFF){
                            param.flashMode = Camera.Parameters.FLASH_MODE_TORCH

                            btnflash.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_flash_on)
                            btnflash.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
                        } else {
                            param.flashMode = Camera.Parameters.FLASH_MODE_OFF

                            btnflash.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_flash_off)
                            btnflash.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray_megadark))
                        }
                        camera.parameters = param
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            (R.id.btnimagen) -> {

            }
            (R.id.btnremove) -> {
                if (seekBar.progress >= 1) {
                    seekBar.progress = seekBar.progress - 10
                }
            }
            (R.id.btnadd) -> {
                if (seekBar.progress < 100) {
                    seekBar.progress = seekBar.progress + 10
                }
            }
        }
    }

    private fun areCameraPermissionsGranted(): Boolean {
        val cameraPermission = Manifest.permission.CAMERA
        val result = ContextCompat.checkSelfPermission(requireContext(), cameraPermission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermissions() {
        val cameraPermission = Manifest.permission.CAMERA

        requestPermissions(arrayOf(cameraPermission), CAMERA_PERMISSION_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner()
            }
        }
    }

    fun startScanner() {

        val detector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(requireContext(), detector)
            .setAutoFocusEnabled(true)
            .build()

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(surfaceView.holder)
                } catch (e: IOException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.release()
            }
        })

        detector.setProcessor(object : Detector.Processor<com.google.android.gms.vision.barcode.Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<com.google.android.gms.vision.barcode.Barcode>) {
                val barcodes = detections.detectedItems
                if ( detected ){
                    val barcode = barcodes.valueAt(0)
                    val vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        vibrator.vibrate(300)
                    }

                    barcodescanned =
                        BarCode(
                            SharedPreferencesManager.getNameScanned(barcode.format),
                            barcode.rawValue,
                            barcode.format,
                            BarCode.ORG_SCANED,
                            System.currentTimeMillis()
                        )

                    MyApplication
                        .getDatabaseInstance()
                        .barcodeDao()
                        .insertBarCode(barcodescanned)

                    detected = false
                }

            }
        })
    }

    private fun getCamera(cameraSource: CameraSource): Camera? {
        val declaredFields = CameraSource::class.java.declaredFields

        for (field in declaredFields) {
            if (field.type == Camera::class.java) {
                field.isAccessible = true
                try {
                    val camera = field.get(cameraSource) as Camera?
                    if (camera != null) {
                        return camera
                    }
                    return null
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }
        return null
    }

}