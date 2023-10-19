package com.albert.avdevdemo.junior

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.albert.avdevdemo.databinding.ActivityJuniorLesson2Binding
import com.albert.avdevdemo.misc.FileUtil
import com.albert.avdevdemo.misc.PcmToWavUtil
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

//使用 AudioRecord 采集音频PCM并保存到文件
class Lesson2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityJuniorLesson2Binding

    //核心AudioRecord对象
    private lateinit var audioRecord: AudioRecord
    private var bufferSize = 1024
    private lateinit var buffer: ByteArray

    @Volatile
    private var isRecording = false
    private var wav: File? = null
    //播放

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJuniorLesson2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        initAudioRecord()
        binding.btnRecord.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    111
                )
            } else {
                startRecord()
            }
        }
        binding.btnStopRecord.setOnClickListener {
            isRecording = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111) {
            startRecord()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startRecord() {
        Thread {
            val p1 = 44100
            val p2 = AudioFormat.CHANNEL_IN_STEREO
            val p3 = AudioFormat.ENCODING_PCM_16BIT
            bufferSize = AudioRecord.getMinBufferSize(
                p1,
                p2,
                p3
            )//参数的东西，文档默认吧
            if (bufferSize < 0) {
                Log.d("record", "bufferSize _ " + bufferSize)
                return@Thread
            }
            buffer = ByteArray(bufferSize)
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                p1,
                p2,
                p3, bufferSize
            )
            audioRecord.startRecording()
            isRecording = true
            //创建一个数据流！！
            //一边从AudioRecord中读取声音数据 到初始化的buffer，一边将buffer中数据 导入到数据流
            val subPath = "/" + "录音测试"
            FileUtil.createFileOnDefaultPath(this, subPath)
            val filePath = FileUtil.getDefaultPath(this) + subPath
            var fileOs: FileOutputStream? = null
            try {
                fileOs = FileOutputStream(filePath)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            if (fileOs == null) return@Thread
            var readResult = 0
            while (isRecording) {
                readResult = audioRecord.read(buffer, 0, bufferSize)//这一步：从系统层读取数据
                Log.d("record", "readResult_ " + readResult)
                //如果从系统层读取音频数据没有出现错误，就可以根据 业务场景，对数据进行后续处理了。
                // 这里是：将数据写入到文件
                if (AudioRecord.ERROR_INVALID_OPERATION != readResult) {
                    try {
                        fileOs.write(buffer)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            //暂停录制
            audioRecord.stop()
            //关闭资源
            try {
                fileOs.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //加音频的 文件头
            val wavPath = filePath + ".wav"
            PcmToWavUtil(p1, p2, bufferSize).pcmToWav(filePath, wavPath)
            wav = File(wavPath)
        }.start()
    }


    private fun initAudioRecord() {
    }

    override fun onDestroy() {
        super.onDestroy()
        audioRecord.release()
    }
}