package com.example.lkproje

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity3 : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        databaseHelper = DatabaseHelper(this)


        val productList: MutableList<Product> = databaseHelper.getAllProducts().toMutableList()


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(productList, databaseHelper, this)
        recyclerView.adapter = productAdapter
    }
}