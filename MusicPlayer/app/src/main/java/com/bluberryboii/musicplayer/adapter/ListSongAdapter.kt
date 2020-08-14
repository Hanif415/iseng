package com.bluberryboii.musicplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluberryboii.musicplayer.model.ModelListSong
import com.bluberryboii.musicplayer.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_list_music.view.*

class ListSongAdapter(
    private val listSong: List<ModelListSong>
) : RecyclerView.Adapter<ListSongAdapter.ListViewHolder>() {

    private var onSelectedData: OnSelectedData? = null

    fun onSelected(onSelectedData: OnSelectedData) {
        this.onSelectedData = onSelectedData
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list_music, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listSong.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) =
        holder.bind(listSong[position])

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(modelListSong: ModelListSong) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(modelListSong.strCoverSong)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.drawable.ic_no_image)
                    .into(imgCover)

                tvBand.text = modelListSong.strNameBand
                tvTitleMusic.text = modelListSong.strSongTitle
                cvListSong.setOnClickListener {
                    onSelectedData?.onSelected(modelListSong)
                }
            }
        }
    }

    interface OnSelectedData {
        fun onSelected(modelListSong: ModelListSong)
    }
}