package com.example.lkproje

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lkproje.databinding.ActivityFirebaseIslemBinding

class FirebaseIslem : AppCompatActivity() {
    lateinit var binding: ActivityFirebaseIslemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseIslemBinding.inflate(layoutInflater)
        var sayfa = binding.root
        setContentView(sayfa)
        binding.buttonurun.setOnClickListener{
            startActivity(Intent(applicationContext, FirebaseUrunGir::class.java))
        }
        binding.buttondgr.setOnClickListener {
            startActivity(Intent(applicationContext, FirebaseUrunDgr::class.java))
        }
    }
}