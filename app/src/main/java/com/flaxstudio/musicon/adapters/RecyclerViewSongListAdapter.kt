package com.flaxstudio.musicon.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flaxstudio.musicon.R
import com.flaxstudio.musicon.rooms.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerViewSongListAdapter(private val contextApp: Context , private val coroutineScope: CoroutineScope,private val itemClickListener: OnItemClickListener) : ListAdapter<Song, RecyclerViewSongListAdapter.CustomViewHolder>(SongItemDiffCallback()) {
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


    inner class CustomViewHolder(itemView: ConstraintLayout, private val itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){

        fun bind(song: Song) {

            // moving text
            itemView.findViewById<TextView>(R.id.songName).apply {
                text = song.title
                isSingleLine = true
//                ellipsize = TextUtils.TruncateAt.MARQUEE
//                isSelected = true
            }

            itemView.findViewById<TextView>(R.id.totalDuration).text = song.duration.toString()
            itemView.findViewById<TextView>(R.id.artistName).text = song.artist


            itemView.setOnClickListener { itemClickListener.onItemClick(song) }
            itemView.findViewById<CheckBox>(R.id.isFav).setOnCheckedChangeListener { _, isChecked ->
                itemClickListener.onFavouriteClick(song, isChecked)
            }
        }

        // Retrieve album art thumbnail from MediaStore

//        val thumbnail: Bitmap =
//            applicationContext.contentResolver.loadThumbnail(
//                content-uri, Size(640, 480), null)



//        Uri albumArtUri = Uri.parse("content://media/external/audio/albumart/" + albumId);
//        Bitmap albumArtBitmap = null;
//        try {
//            InputStream is = context.getContentResolver().openInputStream(albumArtUri);
//            albumArtBitmap = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    class SongItemDiffCallback : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean = oldItem == newItem
    }


}