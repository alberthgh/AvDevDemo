package com.albert.avdevdemo.junior

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.albert.avdevdemo.databinding.ActivityJuniorLesson3Binding
import com.albert.avdevdemo.misc.FileUtil
import java.io.FileInputStream
import java.io.FileNotFoundException

class Lesson3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityJuniorLesson3Binding
    private lateinit var audioTrack: AudioTrack
    private var bufferSize = 0
    private lateinit var buffer: ByteArray
    private var isPlaying = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJuniorLesson3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        createAudioTrack_StreamMode()
        binding.btnPlay.setOnClickListener {
            //不间断通过 write() 写数据给AudioTrack去播放
            play()
        }
    }

    private fun createAudioTrack_StreamMode() {
        val p1 = 44100
        val p2 = AudioFormat.CHANNEL_OUT_STEREO
        val p3 = AudioFormat.ENCODING_PCM_16BIT
        bufferSize = AudioTrack.getMinBufferSize(p1, p2, p3)
        buffer = ByteArray(bufferSize)
        audioTrack =
            AudioTrack(AudioManager.STREAM_MUSIC, p1, p2, p3, bufferSize, AudioTrack.MODE_STREAM)
    }

    private fun play() {
        Thread {
            audioTrack.play()//调用play()。让audioTrack开启播放状态
            val subPath = "/" + "录音测试"
            val filePath = FileUtil.getDefaultPath(this) + subPath
            var fileIs: FileInputStream? = null
            try {
                fileIs = FileInputStream(filePath)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (fileIs == null) return@Thread
            while (fileIs.available() > 0) {
                val readCount = fileIs.read(buffer)
                if (readCount < 0) {
                    break
                }
                //当一个搬运工。把数据从file搬进去audioTrack实例里面
                val writeResult = audioTrack.write(buffer, 0, readCount)
                if (writeResult < 0) {
                    //异常
                    Log.d("Lesson3Activity", "writeResult<0")
                }
            }
            audioTrack.stop()//停止播放
            release()
        }.start()
    }

    private fun release() {
        audioTrack.release()
    }
}