package com.piyush.bookhub.adapter.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.piyush.bookhub.model.Book

@Database(entities = [BookEntity :: class],version = 1)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao



}