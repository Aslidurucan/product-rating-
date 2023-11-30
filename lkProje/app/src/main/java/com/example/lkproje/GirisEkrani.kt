package com.example.lkproje

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class GirisEkrani : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris_ekrani)

        val buttoneposta: Button = findViewById(R.id.buttoneposta)
        val buttontel: Button = findViewById(R.id.buttontel)
        val buttongoogle: Button = findViewById(R.id.buttongoogle)

        buttoneposta.setOnClickListener {

            val intent = Intent(this, GirisYap::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()


       buttontel.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, PhoneActivity::class.java))
        }

        buttongoogle.setOnClickListener {
            val intent = Intent(this, GoogleActivity::class.java)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()
    }
}