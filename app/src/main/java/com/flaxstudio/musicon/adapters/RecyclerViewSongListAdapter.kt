package com.flaxstudio.musicon.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flaxstudio.musicon.R
import com.flaxstudio.musicon.rooms.Song

class RecyclerViewSongListAdapter(private val itemClickListener: OnItemClickListener) : ListAdapter<Song, RecyclerViewSongListAdapter.CustomViewHolder>(SongItemDiffCallback()) {
    interface OnItemClickListener {
        fun onItemClick(song: Song)
        fun onFavouriteClick(song: Song, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_play_track_layout, parent, false) as ConstraintLayout

        return CustomViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class CustomViewHolder(itemView: ConstraintLayout, private val itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){

        fun bind(song: Song) {

            // moving text
            itemView.findViewById<TextView>(R.id.songName).apply {
                text = song.fileName
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.MARQUEE
                isSelected = true
            }

            itemView.setOnClickListener { itemClickListener.onItemClick(song) }
            itemView.findViewById<CheckBox>(R.id.isFav).setOnCheckedChangeListener { _, isChecked ->
                itemClickListener.onFavouriteClick(song, isChecked)
            }
        }

    }

    class SongItemDiffCallback : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean = oldItem == newItem
    }


}