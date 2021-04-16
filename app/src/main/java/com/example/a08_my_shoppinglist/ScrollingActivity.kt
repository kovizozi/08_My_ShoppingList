package com.example.a08_my_shoppinglist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a08_my_shoppinglist.adapter_rv.ShoppingListAdapterClass
import com.example.a08_my_shoppinglist.data.AppDatabase
import com.example.a08_my_shoppinglist.data.ShoppingItemDataClass
import com.example.a08_my_shoppinglist.databinding.ActivityScrollingBinding
import com.example.a08_my_shoppinglist.dialog.NewItemDialog

import kotlin.concurrent.thread

class ScrollingActivity : AppCompatActivity(), NewItemDialog.NewItemDialoogHandler {

    companion object {
        val KEY_EDIT = "KEY_EDIT"
    }
private lateinit var binding: ActivityScrollingBinding
    lateinit var shoppingListAdapterCopy: ShoppingListAdapterClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        intitRecyclerView()
        binding.fab.setOnClickListener {
            NewItemDialog(this).show(supportFragmentManager, getString(R.string.newitemTAG))
        }

    }

    override fun NewItemCreated(item: ShoppingItemDataClass) {
        Thread {
            var itemID =
                AppDatabase.getInstance(this@ScrollingActivity).shoppingDao().insertItems(item)
            item.itemId = itemID
        }.start()
        shoppingListAdapterCopy.AddNewItem(item)
    }

    fun intitRecyclerView() {

        Thread {
            var items = AppDatabase.getInstance(this@ScrollingActivity).shoppingDao().getAllItems()
            runOnUiThread {
                shoppingListAdapterCopy = ShoppingListAdapterClass(this, items)
                binding.recyclerShoppingListView.adapter = shoppingListAdapterCopy
                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
                binding.recyclerShoppingListView.setLayoutManager(layoutManager)

            }

        }.start()
    }

    var editIndex = -1

    fun showEditDialog(shoppingItem: ShoppingItemDataClass, adapterPosition: Int) {
        editIndex = adapterPosition

        val editDialog = NewItemDialog(this)
        val bundle = Bundle()
        bundle.putSerializable(KEY_EDIT,shoppingItem)
        editDialog.arguments = bundle
        editDialog.show(supportFragmentManager,"EDITDIALOG")

    }

    override fun ItemUpdated(item: ShoppingItemDataClass) {
        Thread{
            AppDatabase.getInstance(this@ScrollingActivity).shoppingDao().updateItems(item)

            runOnUiThread {
                shoppingListAdapterCopy.updateItem(item, editIndex)
            }
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // melyik menüt töltsük be
        menuInflater.inflate(R.menu.menu_scrolling,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_new){
            NewItemDialog(this).show(supportFragmentManager, "TAG_NEW_ITEM_DIALOG")
        }
        if(item.itemId == R.id.delete_all){
           shoppingListAdapterCopy.deleteAllItems()
        }

        return true
    }
}