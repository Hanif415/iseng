package com.hanif.foodrecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hanif.foodrecipe.R
import com.hanif.foodrecipe.model.ModelFilter
import kotlinx.android.synthetic.main.list_item_filter_food.view.*

class FilterFoodAdapter(private var items: List<ModelFilter>) :
    RecyclerView.Adapter<FilterFoodAdapter.ViewHolder>() {

    private var onSelectedData: OnSelectedData? = null

    fun onSelected(onSelectedData: OnSelectedData) {
        this.onSelectedData = onSelectedData
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_filter_food, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ModelFilter) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(data.strMealThumb)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_thumb)

                tv_meal.text = data.strMeal
                itemView.setOnClickListener {
                    onSelectedData?.onSelected(data)
                }
            }
        }
    }

    interface OnSelectedData {
        fun onSelected(modelFilter: ModelFilter)
    }
}