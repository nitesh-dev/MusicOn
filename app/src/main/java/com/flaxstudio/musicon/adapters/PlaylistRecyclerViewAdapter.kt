package com.flaxstudio.musicon.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.flaxstudio.musicon.R

class PlaylistRecyclerViewAdapter(private val context : Context) : RecyclerView.Adapter<PlaylistRecyclerViewAdapter.RecyclerViewViewHolder>() {

    inner class RecyclerViewViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        val songPoster : ImageView = itemView.findViewById(R.id.shapeableImageView)
        val songName   : TextView  = itemView.findViewById(R.id.textView4)
        val playlistName : TextView = itemView.findViewById(R.id.textView11)
        val songDuration : TextView = itemView.findViewById(R.id.textView10)
        val likeButton : CheckBox  = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_play_track_layout, parent, false)

        return RecyclerViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {

    }

}