package com.example.lkproje


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUrunDgr : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: FirebaseProductAdapter
    private lateinit var database: FirebaseFirestore
    private val productList = mutableListOf<ProductF>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_urun_dgr)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        database = FirebaseFirestore.getInstance()
        productAdapter = FirebaseProductAdapter(productList, database)
        recyclerView.adapter = productAdapter

        getProductListFromFirestore()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getProductListFromFirestore() {
      // val productList = mutableListOf<ProductF>()

        database.collection("products")
            .get()
            .addOnSuccessListener { result ->
                Log.d("FirebaseUrunDgr", "Successfully retrieved documents: ${result.size()}")
                for (document in result) {
                    val id = document.id
                    val filename = document.getString("filename") ?: ""
                    val productPrice = document.getString("productPrice") ?: "0.0"
                    val price = productPrice.toDoubleOrNull() ?: 0.0
                    val rating = document.getDouble("rating")?.toFloat() ?: 0.0f
                    Log.d(
                        "FirebaseUrunDgr",
                        "Retrieved product: id=$id, filename=$filename, price=$price, rating=$rating"
                    )

                    val product = ProductF(id, filename, price, rating)
                    productList.add(product)
                }
                Log.d("FirebaseUrunDgr", "ProductList size: ${productList.size}")

            //  productAdapter = FirebaseProductAdapter(productList, database)
             // recyclerView.adapter = productAdapter

                productAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUrunDgr", "Error getting documents: ", exception)
            }
    }
}
