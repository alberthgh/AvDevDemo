package com.albert.avdevdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.albert.avdevdemo.databinding.ActivityJuniorBinding
import com.albert.avdevdemo.junior.Lesson1Activity

class JuniorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_junior)
        val binding = ActivityJuniorBinding.inflate(layoutInflater)
        binding.lesson1.setOnClickListener {
            startActivity(Intent(this, Lesson1Activity::class.java))
        }
    }
}