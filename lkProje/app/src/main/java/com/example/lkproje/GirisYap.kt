package com.example.lkproje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.lkproje.databinding.ActivityGirisYapBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GirisYap : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var  binding: ActivityGirisYapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris_yap)
        auth = Firebase.auth
        binding = ActivityGirisYapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.texthere1.setOnClickListener{
            val intent = Intent(this, KayitOl::class.java)
            startActivity(intent)
        }
    }
    fun girisyap(view : View){
        val email = binding.emailhere.text.toString()
        val password = binding.passwordhere.text.toString()
        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Lütfen doğru bir şekilde şifre ve emailinizi giriniz", Toast.LENGTH_LONG).show()

        }else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent= Intent(this, MainOncesiActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}