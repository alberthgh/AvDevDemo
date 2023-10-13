package com.albert.avdevdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.albert.avdevdemo.databinding.ActivityJuniorBinding
import com.albert.avdevdemo.junior.Lesson1Activity
import com.albert.avdevdemo.junior.Lesson2Activity

class JuniorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityJuniorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lesson1.setOnClickListener {
            startActivity(Intent(this, Lesson1Activity::class.java))
        }
        binding.lesson2.setOnClickListener {
            startActivity(Intent(this, Lesson2Activity::class.java))
        }
    }
}