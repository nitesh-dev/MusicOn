package com.flaxstudio.musicon.utils

import android.content.Context

class SharedPreferenceManager {


    // ------------------ Playlist -------------------

    private var openedPlaylistName = ""
    private val playlistNames = ArrayList<String>()

    fun setOpenedPlaylist(name: String){
        openedPlaylistName = name
    }

    fun addPlaylist( name: String){
        playlistNames.add(name)
    }

    fun removePlaylist( name: String){
        playlistNames.remove(name)
    }

    fun renamePlaylist(oldName: String, newName: String){
        for (index in playlistNames.indices){
            if(playlistNames[index] == oldName){
                playlistNames[index] = newName
                break
            }
        }
    }

    fun getPlaylists(context: Context): ArrayList<String> {
        if(playlistNames.isEmpty()) playlistNames.addAll(getPlaylistsNames(context).split("&&"))
        return playlistNames
    }

    fun savePlaylists(context: Context){
        val sp = context.getSharedPreferences("App Data", Context.MODE_PRIVATE) ?: return
        val edit = sp.edit()
        edit.putString("saved_playlists", playlistNames.toString("&&"))
        edit.apply()
    }

     private fun getPlaylistsNames(context: Context): String{
        val sp = context.getSharedPreferences("App Data", Context.MODE_PRIVATE) ?: return ""
        return sp.getString("saved_playlists", "")!!
    }


    // ----------------- Theme --------------

    private var selectedTheme: AppTheme? = null

    fun saveTheme(context: Context, theme: AppTheme){
        val sp = context.getSharedPreferences("App Data", Context.MODE_PRIVATE) ?: return
        val edit = sp.edit()
        edit.putString("app_theme", theme.toString())
        edit.apply()
        selectedTheme = theme
    }

    fun getTheme(context: Context): AppTheme{

        if(selectedTheme != null) return selectedTheme!!
        val sp = context.getSharedPreferences("App Data", Context.MODE_PRIVATE) ?: return AppTheme.PINK
        selectedTheme = sp.getString("app_theme", "PINK")!!.toAppTheme()

        return selectedTheme!!
    }
}

