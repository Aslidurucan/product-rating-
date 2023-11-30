package com.example.lkproje

data class Product(
    val id: Long,
    val imagePath: String,
    val fullFilePath: String,
    val price: Double,
    var rating: Float
)