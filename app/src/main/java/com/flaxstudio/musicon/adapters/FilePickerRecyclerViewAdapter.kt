package com.flaxstudio.musicon.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flaxstudio.musicon.R

class FilePickerRecyclerViewAdapter() : RecyclerView.Adapter<FilePickerRecyclerViewAdapter.FilePickerViewHolder>() {

    inner class FilePickerViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val fileCount : TextView = itemView.findViewById(R.id.tv_file_count)
        val fileName : TextView = itemView.findViewById(R.id.tv_file_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilePickerViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file_picker, parent ,false)
        return FilePickerViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: FilePickerViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}