package com.albert.avdevdemo.junior

import android.graphics.BitmapFactory
import android.graphics.Paint
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.albert.avdevdemo.R
import com.albert.avdevdemo.databinding.ActivityJuniorLesson1Binding

//通过三种方式绘制图片
class Lesson1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityJuniorLesson1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJuniorLesson1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        //故意用Bitmap
        binding.ivWay1.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.cat))
        handleSur()
    }

    private fun handleSur() {
        val sur = binding.surfaceWay2
        //sur 是独立window、独立Surface，需要有专门负责处理的类：holder。callBack留接口回调，给开发者处理
        sur.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                //开始给Canvas绘制Bitmap！

                val paint = Paint().apply {
                    isAntiAlias = true
                    style = Paint.Style.STROKE
                }

                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.cat)
                val canvas = holder.lockCanvas()//锁定画布。
                canvas.drawBitmap(bitmap, 0f, 0f, paint)//执行绘制bitmap
                holder.unlockCanvasAndPost(canvas)//解除锁定，提交canvas

            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                //这里的surfaceView宽高并不会changed
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                //不需处理。因为没有需要"释放"的资源
            }

        })

    }
}