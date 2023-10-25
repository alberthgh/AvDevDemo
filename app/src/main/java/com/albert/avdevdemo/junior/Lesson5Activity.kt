package com.albert.avdevdemo.junior

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.albert.avdevdemo.databinding.ActivityJuniorLesson5Binding

class Lesson5Activity : AppCompatActivity() {
    private lateinit var binding: ActivityJuniorLesson5Binding

    //声明相机参数和成员变量
    private val cameraId = "0"//后摄
    private lateinit var cameraDevice: CameraDevice
    private val cameraThread = HandlerThread("CameraThread").apply { start() }
    private val cameraHandler = Handler(cameraThread.looper)
    private val cameraManager: CameraManager by lazy {
        getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    private val characteristics: CameraCharacteristics by lazy {
        cameraManager.getCameraCharacteristics(cameraId)//相机参数
    }

    private lateinit var mCameraCaptureSession: CameraCaptureSession


    // 获取所有摄像头的CameraID
    fun getCameraIds(): Array<String> {
        return cameraManager.cameraIdList
    }

    fun getCameraOrientationString(cameraId: String): String {
        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)!!
        return when (lensFacing) {
            CameraCharacteristics.LENS_FACING_BACK -> "后摄(Back)"
            CameraCharacteristics.LENS_FACING_FRONT -> "前摄(Front)"
            CameraCharacteristics.LENS_FACING_EXTERNAL -> "外置(External)"
            else -> "Unknown"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJuniorLesson5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.btnStart.setOnClickListener {

//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    arrayOf( Manifest.permission.CAMERA,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.RECORD_AUDIO),
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {

            ActivityCompat.requestPermissions(
                this,

                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                ),
                111
            )
//            } else {
//                startRecord()
//            }
        }

        binding.sur.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                //为了确保设置了大小，需要在主线程中初始化camera
                binding.root.post {
                    openCamera(cameraId)
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }

        })
    }

    @SuppressLint("MissingPermission")
    private fun openCamera(cameraId: String) {
        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
            }

            override fun onDisconnected(camera: CameraDevice) {
                this@Lesson5Activity.finish()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                Toast.makeText(application, "openCamera Failed:$error", Toast.LENGTH_SHORT).show()
            }

        }, cameraHandler)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111) {
//            startRecord()
            startPreview()
        }
    }

    private fun startPreview() {
        //因为摄像头可以同时输出多个流，所以可以传入多个surface
        val targets = listOf(binding.sur.holder.surface)
        cameraDevice.createCaptureSession(targets, object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                //session对象
                mCameraCaptureSession = session
                val captureRequest = cameraDevice.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW
                ).apply {
                    addTarget(binding.sur.holder.surface)
                }
                //这将不断地实时发送视频流，知道会话断开或调用session.stoprepeat()
                mCameraCaptureSession.setRepeatingRequest(
                    captureRequest.build(),
                    null,
                    cameraHandler
                )
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Toast.makeText(application, "session configuration failed", Toast.LENGTH_SHORT)
                    .show()
            }

        }, cameraHandler)
    }

    private fun startRecord() {
        val cameraIds = getCameraIds()
        cameraIds.forEach { cameraId ->
            val orientation = getCameraOrientationString(cameraId)
            Log.i("TAG", "cameraId : $cameraId - $orientation")
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            cameraDevice.close()
        } catch (e: Throwable) {
            Log.e("", "Error closing camera", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraThread.quitSafely()
    }
}