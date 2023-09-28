package com.albert.avdevdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.albert.avdevdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.junior.setOnClickListener {
            startActivity(Intent(this, JuniorActivity::class.java))
        }
        binding.category.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
    }
}