package com.example.lkproje
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lkproje.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var imageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var urunFiyati: EditText
    private lateinit var btnKaydet: Button
    private lateinit var databaseHelper: DatabaseHelper

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val TAG = "MainActivity2"
        private const val PERMISSION_REQUEST_CODE = 123
    }

    private var selectedImagePath: String? = null
    private var fullFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.imageView
        urunFiyati = binding.urunFiyati
        btnKaydet = binding.btnKaydet
        databaseHelper = DatabaseHelper(this)

        selectImageButton = binding.selectImageButton
        selectImageButton.setOnClickListener {
            checkPermissionsAndOpenGallery()
        }

        btnKaydet.setOnClickListener {
            val price = urunFiyati.text.toString().toDoubleOrNull()
            Log.d(TAG, "Kaydet butonuna tıklandı.")
            Log.d(TAG, "selectedImagePath: $selectedImagePath")
            Log.d(TAG, "fullFilePath: $fullFilePath")
            Log.d(TAG, "price: $price")

            if (selectedImagePath != null && fullFilePath != null && price != null) {
                val productID = databaseHelper.addProduct(selectedImagePath!!, fullFilePath!!, price)
                if (productID != -1L) {
                    Log.d(
                        TAG,
                        "Ürün veritabanına kaydedildi. Product ID: $productID, Görsel Path: $selectedImagePath, Tam Dosya Yolu: $fullFilePath, Fiyat: $price"
                    )
                    selectedImagePath = null
                    fullFilePath = null
                    urunFiyati.text.clear()
                } else {
                    Log.d(TAG, "Ürün veritabanına kaydedilemedi.")
                }
            }
        }
    }

    private fun checkPermissionsAndOpenGallery() {
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsToRequest = mutableListOf<String>()

        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImagePath = getRealPathFromUri(uri)
                fullFilePath = uri.toString()
                Log.d(TAG, "Seçilen görselin tam dosya yolu: $fullFilePath")
            }
        }
    }

    private fun getRealPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val displayName = it.getString(columnIndex)
                it.close()
                return displayName
            }
            it.close()
        }
        return null
    }
}



