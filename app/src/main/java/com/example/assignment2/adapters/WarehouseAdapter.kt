package com.example.assignment2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2.R
import com.example.assignment2.models.WarehouseModel
import kotlinx.android.synthetic.main.card_add.view.*


interface WarehouseListener {
    fun onWarehouseClick(warehouse: WarehouseModel)
}

class WarehouseAdapter constructor(var stocks: ArrayList<WarehouseModel>,
                                   private val listener: WarehouseListener)
    : RecyclerView.Adapter<WarehouseAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_add,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val ware = stocks[holder.adapterPosition]
        holder.bind(ware,listener)
    }

    override fun getItemCount(): Int = stocks.size

    fun removeAt(position: Int) {
        stocks.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(warehouse: WarehouseModel, listener: WarehouseListener) {

            itemView.tag = warehouse
            itemView.stockName.text = warehouse.stname
            itemView.stockQuantity.text = warehouse.stquantity
            itemView.country.text = warehouse.stcountry
            itemView.palletType.text = warehouse.pallettype
            itemView.palletNumber.text = warehouse.palletnumber
            itemView.setOnClickListener { listener.onWarehouseClick(warehouse) }
        }
    }
}