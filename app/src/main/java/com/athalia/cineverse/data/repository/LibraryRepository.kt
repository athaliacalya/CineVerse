package com.athalia.cineverse.data.repository

import android.content.Context
import com.athalia.cineverse.data.model.SavedMovie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LibraryRepository(context: Context) {
    private val prefs = context.getSharedPreferences("cineverse_library", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val type = object : TypeToken<List<SavedMovie>>() {}.type

    fun load(): List<SavedMovie> {
        val json = prefs.getString("items", null) ?: return emptyList()
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun save(items: List<SavedMovie>) {
        prefs.edit().putString("items", gson.toJson(items)).apply()
    }
}
