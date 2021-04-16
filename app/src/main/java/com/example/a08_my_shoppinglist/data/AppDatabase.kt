package com.example.a08_my_shoppinglist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a08_my_shoppinglist.R

@Database(entities = arrayOf(ShoppingItemDataClass::class), version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun shoppingDao(): ShoppingDao

    companion object {
        private var INSTANCE: AppDatabase? =null
        fun getInstance(context: Context): AppDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase::class.java,context.getString(R.string.shopping_db_name)).build()
            }
            return INSTANCE!!
        }
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}