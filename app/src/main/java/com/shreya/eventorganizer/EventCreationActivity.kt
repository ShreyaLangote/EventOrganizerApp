package com.shreya.eventorganizer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EventCreationActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_creation)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference

        val btnSave: Button = findViewById(R.id.save_event_button)
        val etEventName: EditText = findViewById(R.id.event_name)
        val etEventLocation: EditText = findViewById(R.id.event_location)
        val etEventDate: EditText = findViewById(R.id.event_date)
        val etEventDescription: EditText = findViewById(R.id.event_description)

        btnSave.setOnClickListener {
            val name = etEventName.text.toString()
            val location = etEventLocation.text.toString()
            val date = etEventDate.text.toString()
            val description = etEventDescription.text.toString()

            if (name.isNotEmpty() && date.isNotEmpty()) {
                saveEvent(name, location, date, description)
            } else {
                if (name.isEmpty()) etEventName.error = "Please enter the event name"
                if (date.isEmpty()) etEventDate.error = "Please enter the event date"
                Toast.makeText(this, "Required fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveEvent(name: String, location: String, date: String, description: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Save event under the global events node, not per user
            val eventRef = FirebaseDatabase.getInstance().reference.child("events").push()

            val event = Event(
                name = name,
                date = date,
                location = location,
                hostId = userId,  // This is the current user's UID
                description = description
            )

            // Save the event in the global 'events' node
            eventRef.setValue(event).addOnSuccessListener {
                Toast.makeText(this, "Event created successfully", Toast.LENGTH_SHORT).show()
                finish()  // Close the event creation activity after success
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to create event", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
}
