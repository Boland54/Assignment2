package com.example.assignment2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.assignment2.R
import com.example.assignment2.adapters.WarehouseAdapter
import com.example.assignment2.adapters.WarehouseListener
import com.example.assignment2.models.WarehouseModel
import com.example.assignment2.utils.createLoader
import com.example.assignment2.utils.hideLoader
import com.example.assignment2.utils.showLoader
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_stock.view.*


import org.jetbrains.anko.info

class StockAllFragment : StockFragment(),
    WarehouseListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_stock, container, false)
        activity?.title = getString(R.string.menu_stock_all)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        setSwipeRefresh()

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StockAllFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
                getAllUsersWarehouses()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getAllUsersWarehouses()
    }

    fun getAllUsersWarehouses() {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading All Users Donations from Firebase")
        val stocksList = ArrayList<WarehouseModel>()
        app.database.child("stocks")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Stock error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val donation = it.
                        getValue<WarehouseModel>(WarehouseModel::class.java)

                        stocksList.add(donation!!)
                        root.recyclerView.adapter =
                            WarehouseAdapter(stocksList, this@StockAllFragment)
                        root.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.database.child("stocks").removeEventListener(this)
                    }
                }
            })
    }
}