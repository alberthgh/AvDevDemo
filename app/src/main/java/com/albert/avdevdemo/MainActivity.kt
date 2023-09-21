package com.albert.avdevdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.albert.avdevdemo.databinding.ActivityMainBinding
import com.albert.avdevdemo.junior.Lesson1Activity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.step1.setOnClickListener {
            startActivity(Intent(this, Lesson1Activity::class.java))
        }
        binding.category.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
    }
}