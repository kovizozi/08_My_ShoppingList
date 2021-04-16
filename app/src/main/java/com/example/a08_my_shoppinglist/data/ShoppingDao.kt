package com.example.a08_my_shoppinglist.data

import androidx.room.*

@Dao interface ShoppingDao {

    @Query("SELECT * FROM sItems")
    fun getAllItems(): List<ShoppingItemDataClass>

    @Insert
    fun insertItems( items: ShoppingItemDataClass) : Long

    @Delete
    fun deleteItems(vararg items: ShoppingItemDataClass)

    @Update
    fun updateItems(vararg items: ShoppingItemDataClass)

}