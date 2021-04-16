package com.example.a08_my_shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "sItems")
data class ShoppingItemDataClass (
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    @ColumnInfo(name ="itemName")var itemName: String,
    @ColumnInfo(name = "purchased") var purchased: Boolean,
    @ColumnInfo(name = "category") var category: Int,
    @ColumnInfo(name = "itemPrice" ) var itemPrice:Double,
    @ColumnInfo(name = "description") var itemDesc: String
) : Serializable