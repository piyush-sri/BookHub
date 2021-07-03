package com.piyush.bookhub.adapter.Database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// it is example of entity class
@Entity(tableName="books")
data class BookEntity (
    @PrimaryKey val book_id : Int,
    @ColumnInfo(name ="book_name") val bookName : String,
    @ColumnInfo(name = "book_author")val bookAuthor: String,
    @ColumnInfo(name = "book_price")val bookPrice : String,
    @ColumnInfo(name = "book_rating")val bookRating:String,
    @ColumnInfo(name = "book_desc")val bookDesc:String,
    @ColumnInfo(name = "book_image")val bookImage: String
)
