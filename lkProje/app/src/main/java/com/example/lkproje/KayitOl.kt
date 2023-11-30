package com.example.lkproje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.lkproje.databinding.ActivityKayitOlBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class KayitOl : AppCompatActivity() {
    private lateinit var binding: ActivityKayitOlBinding

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kayit_ol)
        binding = ActivityKayitOlBinding.inflate(layoutInflater) //xmlle kodumu baglamak icin
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

    }

    fun kayitOl(view: View){
    val email = binding.emailhere.text.toString()
        val password = binding.passwordhere.text.toString()
        if(email.equals("") || password.equals("")){
            Toast.makeText(this, "Doğru bir şekilde email ve şifreyi giriniz.",Toast.LENGTH_LONG).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener{
                val intent = Intent(this, GirisYap::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG). show()
            }
        }
    }
}