package com.example.a08_my_shoppinglist.adapter_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a08_my_shoppinglist.R
import com.example.a08_my_shoppinglist.ScrollingActivity
import com.example.a08_my_shoppinglist.data.AppDatabase
import com.example.a08_my_shoppinglist.data.ShoppingItemDataClass
import com.example.a08_my_shoppinglist.databinding.ListRowLayoutBinding
import com.example.a08_my_shoppinglist.databinding.NewItemDialogBinding


class ShoppingListAdapterClass :
    RecyclerView.Adapter<ShoppingListAdapterClass.ViewHolderInnerClass> {

    var shoppingItems = mutableListOf<ShoppingItemDataClass>()
    val context: Context

    constructor(context: Context, items: List<ShoppingItemDataClass>) : super() {
        this.context = context
        shoppingItems.addAll(items)
    }

    inner class ViewHolderInnerClass(val binding: ListRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var tvNameInClass = binding.tvName
        var cbPurchasedInClass = binding.cbPurchased
        var btnDelInClass = binding.btnDel
        var ivCategoryInClass = binding.ivCategory
        var btnInfoInClass = binding.btnInfo
        var tvPriceInClass = binding.tvPrice

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInnerClass {
        //hogy néz ki egy lista elem
        val listRowView =
            ListRowLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolderInnerClass(listRowView)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(holder: ViewHolderInnerClass, position: Int) {
        // itt meg kell mondani, hogy az egyes listaelemeknél mit rajzoljon ki
        var sListItem = shoppingItems[position]
        holder.tvNameInClass.text = sListItem.itemName
        holder.cbPurchasedInClass.isChecked = sListItem.purchased
        holder.tvPriceInClass.text = sListItem.itemPrice.toString()


        if (sListItem.category == 0){
            holder.ivCategoryInClass.setImageResource(android.R.drawable.ic_menu_help)
        }
        else if (sListItem.category == 1){
            holder.ivCategoryInClass.setImageResource(R.drawable.food)
        }
        else if (sListItem.category == 2){
            holder.ivCategoryInClass.setImageResource(R.drawable.book)
        }
        else if (sListItem.category == 3){
            holder.ivCategoryInClass.setImageResource(R.drawable.electronic)
        }
        else if (sListItem.category == 4){
            holder.ivCategoryInClass.setImageResource(R.drawable.clothes)
        }

        holder.btnDelInClass.setOnClickListener {
            deleteShoppingItem(holder.adapterPosition)
        }
        holder.cbPurchasedInClass.setOnClickListener {
            updateCb(holder)
        }
        holder.btnInfoInClass.setOnClickListener {
            (context as ScrollingActivity).showEditDialog(shoppingItems[holder.adapterPosition],holder.adapterPosition)
        }
        holder.tvNameInClass.setOnClickListener{
            (context as ScrollingActivity).showEditDialog(shoppingItems[holder.adapterPosition],holder.adapterPosition)
        }
    }



    private fun updateCb(holder: ViewHolderInnerClass) {
        shoppingItems[holder.adapterPosition].purchased = holder.cbPurchasedInClass.isChecked
        val actItem = shoppingItems[holder.adapterPosition]
        Thread {
            AppDatabase.getInstance(context).shoppingDao().updateItems(actItem)
            (context as ScrollingActivity).runOnUiThread {
                notifyItemChanged(holder.adapterPosition)
            }
        }.start()
    }

    fun deleteShoppingItem(position: Int) {
        var toDeleteItem = shoppingItems[position]
        Thread {
            AppDatabase.getInstance(context).shoppingDao().deleteItems(toDeleteItem)

            (context as ScrollingActivity).runOnUiThread {
                shoppingItems.removeAt(position)
                notifyItemRemoved(position)
            }
        }.start()

    }

    fun AddNewItem(item: ShoppingItemDataClass) {
        shoppingItems.add(item)
        notifyItemInserted(shoppingItems.lastIndex)
    }

    fun updateItem(item: ShoppingItemDataClass, editIndex: Int) {
    shoppingItems.set(editIndex,item)

        notifyItemChanged(editIndex)
    }
    fun deleteAllItems(){

        Thread {
            for (item in shoppingItems){
                AppDatabase.getInstance(context).shoppingDao().deleteItems(item)
        }
            (context as ScrollingActivity).runOnUiThread {
                while(shoppingItems.size>0) {
                    shoppingItems.removeAt(0)
                }
                notifyDataSetChanged()


            }


        }.start()
    }


}