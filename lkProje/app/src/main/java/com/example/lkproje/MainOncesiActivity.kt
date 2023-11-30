package com.example.lkproje

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lkproje.databinding.ActivityMainOncesiBinding

class MainOncesiActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainOncesiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainOncesiBinding.inflate(layoutInflater)
        var sayfa = binding.root
        setContentView(sayfa)
        binding.cloud.setOnClickListener{
            startActivity(Intent(applicationContext, FirebaseIslem::class.java))
        }
        binding.local.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

    }
}