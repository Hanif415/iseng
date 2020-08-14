package com.hanif.foodrecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hanif.foodrecipe.R
import com.hanif.foodrecipe.model.ModelMain
import kotlinx.android.synthetic.main.list_item_categories.view.*

class MainAdapter(private var items: List<ModelMain>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var onSelectedData: OnSelectedData? = null

    fun onSelected(onSelectedData: OnSelectedData) {
        this.onSelectedData = onSelectedData
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_categories, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ModelMain) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(data.strCategoryThumb)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_category)

                tv_category.text = data.strCategory

                itemView.setOnClickListener {
                    onSelectedData!!.onSelected(data)
                }
            }
        }
    }

    interface OnSelectedData {
        fun onSelected(modelMain: ModelMain)
    }
}