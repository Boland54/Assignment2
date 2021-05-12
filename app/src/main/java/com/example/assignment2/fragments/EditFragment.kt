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
import com.example.assignment2.utils.createLoader
import com.example.assignment2.utils.hideLoader
import com.example.assignment2.utils.showLoader
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.fragment_edit.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditFragment : Fragment(), AnkoLogger {

    lateinit var app: WarehouseApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editWarehouse: WarehouseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as WarehouseApp

        arguments?.let {
            editWarehouse = it.getParcelable("editwarehouse")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_edit, container, false)
        activity?.title = getString(R.string.action_edit)
        loader = createLoader(activity!!)

        root.editName.setText(editWarehouse!!.stname)
        root.editQuantity.setText(editWarehouse!!.stquantity)
        root.editCountry.setText(editWarehouse!!.stcountry)
        root.editPType.setText(editWarehouse!!.pallettype)
        root.editPNumber.setText(editWarehouse!!.palletnumber)


        root.editUpdateButton.setOnClickListener {
            showLoader(loader, "Updating Stock on Server...")
            updateDonationData()
            updateDonation(editWarehouse!!.uid, editWarehouse!!)
            updateUserDonation(app.auth.currentUser!!.uid,
                               editWarehouse!!.uid, editWarehouse!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(warehouse: WarehouseModel) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("editwarehouse",warehouse)
                }
            }
    }

    fun updateDonationData() {
        editWarehouse!!.stname = root.editName.text.toString()
        editWarehouse!!.stquantity = root.editQuantity.text.toString()
        editWarehouse!!.stcountry = root.editCountry.text.toString()
        editWarehouse!!.pallettype = root.editPType.text.toString()
        editWarehouse!!.palletnumber = root.editPNumber.text.toString()
    }

    fun updateUserDonation(userId: String, wid: String?, warehouse: WarehouseModel) {
        app.database.child("user-stocks").child(userId).child(wid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(warehouse)
                        activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFrame, StockFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Stock error : ${error.message}")
                    }
                })
    }

    fun updateDonation(wid: String?, warehouse: WarehouseModel) {
        app.database.child("stocks").child(wid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(warehouse)
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Stock error : ${error.message}")
                    }
                })
    }
}
