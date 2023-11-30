package com.example.lkproje
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "product_database"
        private const val TABLE_PRODUCTS = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_IMAGE_PATH = "image_path"
        private const val COLUMN_FULL_FILE_PATH = "full_file_path"
        private const val COLUMN_PRODUCT_PRICE = "product_price"
        private const val COLUMN_PRODUCT_RATING = "product_rating"
        private const val TAG = "DatabaseHelper"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE $TABLE_PRODUCTS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_IMAGE_PATH TEXT, $COLUMN_FULL_FILE_PATH TEXT, $COLUMN_PRODUCT_PRICE REAL, $COLUMN_PRODUCT_RATING REAL)"
        db.execSQL(createTableQuery)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {

            db.execSQL("ALTER TABLE $TABLE_PRODUCTS ADD COLUMN $COLUMN_FULL_FILE_PATH TEXT")
        }
    }

    fun addProduct(imagePath: String, fullFilePath: String, productPrice: Double): Long {
        val values = ContentValues()
        values.put(COLUMN_IMAGE_PATH, imagePath)
        values.put(COLUMN_FULL_FILE_PATH, fullFilePath)
        values.put(COLUMN_PRODUCT_PRICE, productPrice)
        return writableDatabase.insert(TABLE_PRODUCTS, null, values)
    }
    fun updateProductRating(id: Long, rating: Float) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PRODUCT_RATING, rating)
        db.update(TABLE_PRODUCTS, values, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val query = "SELECT * FROM $TABLE_PRODUCTS"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)

        try {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                    val imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH))
                    val price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE))
                    val rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_PRODUCT_RATING))
                    val fullFilePath = "/storage/emulated/0/Download/$imagePath"
                    val product = Product(id, imagePath, fullFilePath, price, rating)
                    productList.add(product)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while getting products from the database", e)
        } finally {
            cursor?.close()
        }

        return productList
    }

}