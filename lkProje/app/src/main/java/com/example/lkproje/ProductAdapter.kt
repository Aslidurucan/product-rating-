package com.example.lkproje

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.util.EnumMap

class ProductAdapter(private val productList: MutableList<Product>, private val databaseHelper: DatabaseHelper, private val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)

        val qrCodeImageView: ImageView = itemView.findViewById(R.id.qrCodeImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]


        val imagePath = product.fullFilePath


        if (imagePath != null) {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                holder.imageView.setImageBitmap(bitmap)
            } else {
                Log.e("ProductAdapter", "Dosya bulunamadı: $imagePath")
            }
        } else {
            Log.e("ProductAdapter", "Dosya yolu null")
        }
        holder.ratingBar.rating = product.rating
        holder.ratingBar.setOnRatingBarChangeListener{ratingbar, rating , fromUser ->
            product.rating= rating
            databaseHelper.updateProductRating(product.id , rating)
            Log.d("ProductAdapter", "Product ID: ${product.id}, New Rating: $rating")
        }
        holder.priceTextView.text = "Product Price: ${product.price}"

        val qrCodeBitmap = generateQRCodeForProduct(product)
        if (qrCodeBitmap != null) {
            holder.qrCodeImageView.setImageBitmap(qrCodeBitmap)
        } else {

            holder.qrCodeImageView.setImageResource(R.drawable.ic_launcher_background)
        }
    }


    override fun getItemCount(): Int {
        return productList.size
    }
    private fun generateQRCodeForProduct(product: Product): Bitmap? {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        val productInfo = "ProductID: ${product.id}\n" +
                "Image Path: ${product.fullFilePath}\n" +
                "Product Price: ${product.price}\n" +
                "Product Rating: ${product.rating}"

        try {
            val qrCodeWriter = QRCodeWriter()
            val bitMatrix = qrCodeWriter.encode(productInfo, BarcodeFormat.QR_CODE, 500, 500, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)

            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
                }
            }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}