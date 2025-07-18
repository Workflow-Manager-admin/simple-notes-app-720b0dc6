package com.example.notesfrontend

import com.example.notesfrontend.BuildConfig
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONObject

/**
 * Service to perform CRUD operations on notes table in Supabase via its REST API.
 */
class SupabaseNoteService {
    private val supabaseUrl: String
        get() = BuildConfig.SUPABASE_URL
    private val supabaseKey: String
        get() = BuildConfig.SUPABASE_KEY
    private val notesEndpoint: String
        get() = "$supabaseUrl/rest/v1/notes"

    // PUBLIC_INTERFACE
    /**
     * Fetch list of notes from Supabase.
     */
    fun fetchNotes(): List<Note> {
        val conn = URL(notesEndpoint).openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("apikey", supabaseKey)
        conn.setRequestProperty("Authorization", "Bearer $supabaseKey")
        conn.setRequestProperty("Accept", "application/json")
        val response = conn.inputStream.bufferedReader().use { it.readText() }
        val json = JSONArray(response)
        val notes = mutableListOf<Note>()
        for (i in 0 until json.length()) {
            val obj = json.getJSONObject(i)
            notes.add(
                Note(
                    id = obj.getString("id"),
                    title = obj.getString("title"),
                    content = obj.getString("content"),
                    createdAt = obj.optLong("created_at", System.currentTimeMillis())
                )
            )
        }
        return notes
    }

    // PUBLIC_INTERFACE
    /**
     * Create a new note in Supabase.
     */
    fun createNote(title: String, content: String) {
        val obj = JSONObject()
        obj.put("title", title)
        obj.put("content", content)
        postOrPatch(notesEndpoint, obj, "POST")
    }

    // PUBLIC_INTERFACE
    /**
     * Update an existing note.
     */
    fun updateNote(note: Note) {
        val url = "$notesEndpoint?id=eq.${note.id}"
        val obj = JSONObject()
        obj.put("title", note.title)
        obj.put("content", note.content)
        postOrPatch(url, obj, "PATCH")
    }

    // PUBLIC_INTERFACE
    /**
     * Delete a note.
     */
    fun deleteNote(note: Note) {
        val url = "$notesEndpoint?id=eq.${note.id}"
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.requestMethod = "DELETE"
        conn.setRequestProperty("apikey", supabaseKey)
        conn.setRequestProperty("Authorization", "Bearer $supabaseKey")
        conn.inputStream.close() // Read response to complete the request
    }

    private fun postOrPatch(url: String, body: JSONObject, method: String) {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.requestMethod = method
        conn.doOutput = true
        conn.setRequestProperty("apikey", supabaseKey)
        conn.setRequestProperty("Authorization", "Bearer $supabaseKey")
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Prefer", "return=representation")
        conn.outputStream.use { it.write(body.toString().toByteArray()) }
        conn.inputStream.close() // Just to ensure request is sent
    }
}
