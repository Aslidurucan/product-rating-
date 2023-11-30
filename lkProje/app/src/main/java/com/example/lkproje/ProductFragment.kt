package com.example.lkproje

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductFragment : Fragment() {
    private lateinit var productAdapter: ProductAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_product, container, false)


        databaseHelper = DatabaseHelper(requireContext())


        val productList: MutableList<Product> = databaseHelper.getAllProducts().toMutableList()



        productAdapter = ProductAdapter(productList,databaseHelper,requireContext())

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = productAdapter



        return rootView
    }
}