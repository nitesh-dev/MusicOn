package com.flaxstudio.musicon.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flaxstudio.musicon.R

class PlaylistRecyclerViewAdapter() : RecyclerView.Adapter<PlaylistRecyclerViewAdapter.RecyclerViewViewHolder>() {

    inner class RecyclerViewViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        val songPoster : ImageView = itemView.findViewById(R.id.songIcon)
        val songName   : TextView  = itemView.findViewById(R.id.songName)
        val playlistName : TextView = itemView.findViewById(R.id.artistName)
        val songDuration : TextView = itemView.findViewById(R.id.totalDuration)
        val likeButton : CheckBox  = itemView.findViewById(R.id.isFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_play_track_layout, parent, false)

        return RecyclerViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
        return 3
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        //TODO do the actual implementation of ONBind
        holder.playlistName.text = "Chill Music"
        holder.songDuration.text = "05:35"
        holder.songName.text = "God's Plan"
    }

}