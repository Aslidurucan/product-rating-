package com.example.lkproje

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInButton = findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
            Log.d("GoogleActivity", "Sign-in button clicked, initiating sign-in process")
            signOut() // Önceki oturumu kapat
            signIn()   // Yeni oturumu aç
        }
    }

    private fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener(this) {
            Log.d("GoogleActivity", "Previous user signed out")
            // Önceki oturumu başarıyla kapattığınızda yapılacak işlemler
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            Log.d("GoogleActivity", "Received result from Google Sign-In")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("GoogleActivity", "Google sign in failed: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d("GoogleActivity", "Authenticating with Firebase using Google credentials")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("GoogleActivity", "Firebase authentication successful. User: ${user?.displayName}")
                    startActivity(Intent(this, MainOncesiActivity::class.java))
                    finish()
                } else {
                    Log.e("GoogleActivity", "Firebase authentication failed")
                }
            }
    }
}

