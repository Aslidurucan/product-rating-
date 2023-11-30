package com.example.lkproje

data class ProductF(
    val id: String,
    val dosyaAdi: String, // Dosya adını temsil etmek için yeni bir alan eklendi
    val price: Double,
    var rating: Float
)
