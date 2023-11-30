package com.example.lkproje

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lkproje.databinding.ActivityFirebaseUrunGirBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseUrunGir : AppCompatActivity() {

    private lateinit var binding: ActivityFirebaseUrunGirBinding
    private var imageReference: StorageReference = FirebaseStorage.getInstance().reference
    private var selectedFile: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseUrunGirBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val etProductPrice = binding.etProductPrice

        binding.firebaseImage.setOnClickListener{
            Log.d("FirebaseUrunGir", "Select image button clicked")
            Intent(Intent.ACTION_GET_CONTENT).also{
                it.type = "image/*"
                imageLauncher.launch(it)
            }
        }

        binding.btnUpload.setOnClickListener{
            Log.d("FirebaseUrunGir", "Upload button clicked")
            selectedFile?.let { fileUri ->
                val productPrice = etProductPrice.text.toString()
                uploadImageToStorage(fileUri, productPrice)
            }
        }
    }

    private val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK){
            result?.data?.data?.let{
                selectedFile = it
                binding.firebaseImage.setImageURI(it)
            }
        } else {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToStorage(fileUri: Uri, productPrice: String){
        try{
            val filename = getFileNameFromUri(fileUri) // Get the real filename
            val storageRef = imageReference.child("images/$filename")

            val uploadTask = storageRef.putFile(fileUri)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri.toString()
                        saveProductToFirestore(filename, imageUrl, productPrice)
                        Toast.makeText(this, "Success upload", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error on upload", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error on upload", Toast.LENGTH_SHORT).show()
            }
        } catch(e: Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProductToFirestore(filename: String, imageUrl: String, productPrice: String) {
        val db = FirebaseFirestore.getInstance()
        val productData = hashMapOf(
            "filename" to filename,
            "imageUrl" to imageUrl,
            "productPrice" to productPrice
        )

        db.collection("products")
            .add(productData)
            .addOnSuccessListener {
                Log.d("FirebaseUrunGir", "Product data added to Firestore")
            }
            .addOnFailureListener {
                Log.e("FirebaseUrunGir", "Error adding product data to Firestore: ${it.message}")
            }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex != -1) {
                    return it.getString(columnIndex)
                }
            }
        }
        return "product_${System.currentTimeMillis()}"
    }
}

