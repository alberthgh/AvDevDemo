package com.albert.avdevdemo.junior

import android.graphics.BitmapFactory
import android.os.Bundle
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
    }
}