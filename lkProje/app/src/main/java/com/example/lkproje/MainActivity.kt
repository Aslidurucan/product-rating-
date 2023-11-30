package com.example.lkproje

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lkproje.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        var sayfam = binding.root
        setContentView(sayfam)
        binding.button.setOnClickListener{
            val sayfa2 = Intent(applicationContext, MainActivity2::class.java)
            startActivity(sayfa2)
        }
        binding.button2.setOnClickListener{
            val sayfa3 = Intent(applicationContext, MainActivity3::class.java)
            startActivity(sayfa3)
        }

    }
}