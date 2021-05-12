package com.example.assignment2.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.assignment2.R
import com.example.assignment2.adapters.WarehouseAdapter
import com.example.assignment2.adapters.WarehouseListener
import com.example.assignment2.main.WarehouseApp
import com.example.assignment2.models.WarehouseModel
import com.example.assignment2.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_stock.view.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class StockFragment : Fragment(), AnkoLogger,
                    WarehouseListener {

    lateinit var app: WarehouseApp
    lateinit var loader : AlertDialog
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as WarehouseApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_stock, container, false)
        activity?.title = getString(R.string.action_stock)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.recyclerView.adapter as WarehouseAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                deleteWarehouse((viewHolder.itemView.tag as WarehouseModel).uid)
                deleteUserWarehouse(app.auth.currentUser!!.uid,
                    (viewHolder.itemView.tag as WarehouseModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onWarehouseClick(viewHolder.itemView as WarehouseModel)
            }
        }

        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.recyclerView)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StockFragment().apply {
                arguments = Bundle().apply { }
            }
    }

  open fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
                getAllWarehouses(app.auth.currentUser!!.uid)
            }
        })
    }

    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }


    fun deleteUserWarehouse(userId: String, wid: String?) {
        app.database.child("user-stocks").child(userId).child(wid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Stock error : ${error.message}")
                    }
                })
    }


    fun deleteWarehouse(wid: String?) {
        app.database.child("stocks").child(wid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Stock error : ${error.message}")
                    }
                })
    }


    override fun onWarehouseClick(warehouse: WarehouseModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditFragment.newInstance(warehouse))
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        getAllWarehouses(app.auth.currentUser!!.uid)
    }

    fun getAllWarehouses(userId: String?) {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading Stock from Firebase")
        val warehousesList = ArrayList<WarehouseModel>()
        app.database.child("user-stocks").child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Stock error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val warehouse = it.
                        getValue<WarehouseModel>(WarehouseModel::class.java)

                        warehousesList.add(warehouse!!)
                        root.recyclerView.adapter =
                            WarehouseAdapter(warehousesList, this@StockFragment)
                        root.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.database.child("user-stocks").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }
}
