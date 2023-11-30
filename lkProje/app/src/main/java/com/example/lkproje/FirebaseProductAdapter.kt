package com.example.lkproje

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.squareup.picasso.Picasso
import java.util.EnumMap

class FirebaseProductAdapter(
    private val productList: MutableList<ProductF>,
    private val db: FirebaseFirestore
) : RecyclerView.Adapter<FirebaseProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fimageView)
        val priceTextView: TextView = itemView.findViewById(R.id.fpriceTextView)
        val ratingBar: RatingBar = itemView.findViewById(R.id.fratingBar)
        val qrCodeImageView: ImageView = itemView.findViewById(R.id.fqrCodeImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_row2, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.priceTextView.text = "Product Price: ${product.price}"
        holder.ratingBar.rating = product.rating

        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("images/${product.dosyaAdi}")

        imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
            // Picasso, Glide veya diğer kütüphaneleri kullanarak ImageView'e yüklemek
            Picasso.get().load(imageUrl).into(holder.imageView)
        }.addOnFailureListener { exception ->
            // Görsel indirme hatası durumunda burada işlemler yapılabilir
            Log.e("FirebaseProductAdapter", "Error fetching image: ${exception.message}")
        }

        holder.ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                product.rating = rating
                db.collection("products")
                    .document(product.id.toString())
                    .update("rating", rating)
                    .addOnSuccessListener {
                        Log.d("FirebaseProductAdapter", "Product rating updated")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirebaseProductAdapter", "Error updating product rating", e)
                    }
            }
        }

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

    private fun generateQRCodeForProduct(product: ProductF): Bitmap? {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        val productInfo = "ProductID: ${product.id}\n" +
                "Image Path: ${product.dosyaAdi}\n" +
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