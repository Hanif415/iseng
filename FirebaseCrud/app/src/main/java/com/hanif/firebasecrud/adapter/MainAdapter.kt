package com.hanif.firebasecrud.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.hanif.firebasecrud.R
import com.hanif.firebasecrud.holder.MainViewHolder
import com.hanif.firebasecrud.model.StuffModel

class MainAdapter(private val context: Context, private val stuffList: ArrayList<StuffModel>?) :
    RecyclerView.Adapter<MainViewHolder>() {

    private val listener: FirebaseDataListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_stuff_item, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stuffList?.size!!
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val generator = ColorGenerator.MATERIAL //Costum Color
        val color = generator.randomColor
        holder.view.setCardBackgroundColor(color)
        holder.stuffName.text = "Name   : " + (stuffList?.get(position)?.name)
        holder.stuffBrand.text = "Brand   : " + (stuffList?.get(position)?.brand)
        holder.stuffPrice.text = "Price   : " + (stuffList?.get(position)?.price)
        holder.view.setOnClickListener{ listener.onDataClick(stuffList?.get(position), position)}
    }

    interface FirebaseDataListener {
        fun onDataClick(stuff: StuffModel?, position: Int)
    }

    init {
        listener = context as FirebaseDataListener
    }
}