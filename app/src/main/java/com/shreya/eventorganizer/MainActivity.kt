package com.shreya.eventorganizer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: EventAdapter
    private val eventList = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Pass the onDeleteEvent logic to the adapter
        adapter = EventAdapter(eventList) { event ->
            onDeleteEvent(event)
        }
        recyclerView.adapter = adapter

        // FloatingActionButton to navigate to EventCreationActivity
        findViewById<FloatingActionButton>(R.id.fab_add_event).setOnClickListener {
            val intent = Intent(this, EventCreationActivity::class.java)
            startActivity(intent)
        }

        // Load events from Firebase
        loadEventsFromFirebase()
    }

    private fun loadEventsFromFirebase() {
        database.child("events").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear() // Clear the list to avoid duplicates
                for (eventSnapshot in snapshot.children) {
                    val event = eventSnapshot.getValue(Event::class.java)
                    event?.let { eventList.add(it) }
                }
                adapter.notifyDataSetChanged() // Notify the adapter of data changes
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load events: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Handle event deletion
    private fun onDeleteEvent(event: Event) {
        // Remove the event from the local list
        val position = eventList.indexOf(event)
        if (position != -1) {
            eventList.removeAt(position)
            adapter.notifyItemRemoved(position)

            // Also delete from Firebase
            deleteEventFromFirebase(event)
        }
    }

    // Function to delete event from Firebase
    private fun deleteEventFromFirebase(event: Event) {
        database.child("events").child(event.id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Event deleted from Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error deleting event: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
