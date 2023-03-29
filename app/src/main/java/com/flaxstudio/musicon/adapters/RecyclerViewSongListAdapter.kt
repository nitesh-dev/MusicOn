package com.flaxstudio.musicon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flaxstudio.musicon.R
import com.flaxstudio.musicon.rooms.Song

class RecyclerViewSongListAdapter : ListAdapter<Song, RecyclerViewSongListAdapter.CustomViewHolder>(SongItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_play_track_layout, parent, false) as ConstraintLayout

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class CustomViewHolder(itemView: ConstraintLayout) : RecyclerView.ViewHolder(itemView){

        fun bind(song: Song) {
            itemView.findViewById<TextView>(R.id.songName).text = song.fileName
        }

    }

    class SongItemDiffCallback : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean = oldItem == newItem

    }


}