package com.hanif.firebasecrud.holder

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hanif.firebasecrud.R

class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    @JvmField
    var stuffName: TextView = itemView.findViewById(R.id.stuff_name)

    @JvmField
    var stuffBrand: TextView = itemView.findViewById(R.id.edt_stuff_brand)

    @JvmField
    var stuffPrice: TextView = itemView.findViewById(R.id.edt_stuff_price)

    @JvmField
    var view: CardView = itemView.findViewById(R.id.cv_main)

}