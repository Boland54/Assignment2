package com.example.assignment2.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.assignment2.R
import com.example.assignment2.main.WarehouseApp
import com.example.assignment2.models.WarehouseModel
import com.example.assignment2.utils.*

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.card_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.lang.String.format
import java.util.HashMap


class AddFragment : Fragment(), AnkoLogger {

    lateinit var app: WarehouseApp
    lateinit var loader : AlertDialog
    var stock = WarehouseModel()
    lateinit var eventListener : ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as WarehouseApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_add, container, false)
        loader = createLoader(activity!!)
        activity?.title = getString(R.string.action_add)

        setButtonListener(root)
        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    fun setButtonListener( layout: View) {
        layout.btnAdd.setOnClickListener {
            stock.stname =
                stockName.text.toString()
            stock.stquantity =
                stockQuantity.text.toString()
            stock.stcountry =
                country.text.toString()
            stock.pallettype =
                palletType.text.toString()
            stock.palletnumber =
                palletNumber.text.toString()

            writeNewWarehouse(WarehouseModel(stname = stock.stname, stquantity = stock.stquantity,
            stcountry = stock.stcountry, pallettype = stock.pallettype, palletnumber = stock.palletnumber))
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        app.database.child("user-stocks")
            .child(app.auth.currentUser!!.uid)
//           .removeEventListener(eventListener)
    }

    fun writeNewWarehouse(warehouse: WarehouseModel) {
        // Create new donation at /donations & /donations/$uid
        showLoader(loader, "Adding stock to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.auth.currentUser!!.uid
        val key = app.database.child("stocks").push().key
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        warehouse.uid = key
        val warehouseValues = warehouse.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/stocks/$key"] = warehouseValues
        childUpdates["/user-stocks/$uid/$key"] = warehouseValues

        app.database.updateChildren(childUpdates)
        hideLoader(loader)
    }




}
