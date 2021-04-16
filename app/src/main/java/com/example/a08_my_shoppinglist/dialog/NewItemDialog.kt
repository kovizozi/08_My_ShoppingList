package com.example.a08_my_shoppinglist.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.a08_my_shoppinglist.R
import com.example.a08_my_shoppinglist.ScrollingActivity
import com.example.a08_my_shoppinglist.data.ShoppingItemDataClass
import com.example.a08_my_shoppinglist.databinding.NewItemDialogBinding
import com.google.android.material.textfield.TextInputEditText

class NewItemDialog(var cont: Context) : DialogFragment() {
    interface NewItemDialoogHandler {
        fun NewItemCreated(item: ShoppingItemDataClass)
        fun ItemUpdated(item: ShoppingItemDataClass)
    }

    lateinit var newitemHandlerContext: NewItemDialoogHandler
    var categoryTemp: Int = 0
    private var EDIT_MODE = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NewItemDialoogHandler) {
            newitemHandlerContext = context
        } else {
            throw RuntimeException(getString(R.string.except_dialog))
        }
    }

    lateinit var newItemName: TextInputEditText
    lateinit var spinnerItem: Spinner
    lateinit var ItemPrice: TextInputEditText
    lateinit var ItemDesc: TextInputEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (this.arguments != null && this.arguments!!.containsKey(ScrollingActivity.KEY_EDIT)) {
            EDIT_MODE = true
        }
        val builder =AlertDialog.Builder(context)
        if (EDIT_MODE) {
            builder.setTitle(getString(R.string.editItem))
        } else {
            builder.setTitle(getString(R.string.addNewITem))
        }
        val rootView =NewItemDialogBinding.inflate(LayoutInflater.from(context))


        newItemName = rootView.itemNameIn
        spinnerItem = rootView.categorySpinner
        ItemPrice = rootView.etItemPrice
        ItemDesc = rootView.etItemDesc

        // init category spinner
        var categorySpinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.categoriesDemo, android.R.layout.simple_spinner_item
        )
        categorySpinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerItem.adapter = categorySpinnerAdapter

        builder.setPositiveButton("OK")
        { dialog, which ->
            /// .. máshol validálunk
        }
        builder.setView(rootView.root)
        if (EDIT_MODE) {
            var shopItem =
                this.arguments!!.getSerializable(ScrollingActivity.KEY_EDIT) as ShoppingItemDataClass
            newItemName.setText(shopItem.itemName)
            spinnerItem.setSelection(shopItem.category)
            ItemPrice.setText(shopItem.itemPrice.toString())
            ItemDesc.setText(shopItem.itemDesc)
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        newItemName.requestFocus()
        val inputMethodManager =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            if (newItemName.text!!.isNotEmpty()) {
                if (EDIT_MODE) {
                    HandleItemUpdate()
                } else {
                    HandleItemCreate()
                }
                dialog.dismiss()
            } else {
                newItemName.error = getString(R.string.canNotBeEmpty)
            }
        }


    }

    private fun HandleItemUpdate() {
        var shoppItemToEdit = this.arguments!!.getSerializable(ScrollingActivity.KEY_EDIT) as ShoppingItemDataClass
        shoppItemToEdit.itemName= newItemName.text.toString()
        shoppItemToEdit.itemDesc= ItemDesc.text.toString()
        shoppItemToEdit.itemPrice=ItemPrice.text.toString().toDouble()
        shoppItemToEdit.category = spinnerItem.selectedItemPosition
        newitemHandlerContext.ItemUpdated(shoppItemToEdit)

    }

    fun HandleItemCreate() {
        var newItem = ShoppingItemDataClass(
            null,
            newItemName.text.toString(),
            false,
            spinnerItem.selectedItemPosition,
            ItemPrice.text.toString().toDouble(),
            ItemDesc.text.toString()
        )
        newitemHandlerContext.NewItemCreated(newItem)

    }


}