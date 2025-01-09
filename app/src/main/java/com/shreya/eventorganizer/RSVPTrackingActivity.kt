package com.shreya.eventorganizer

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.shreya.eventorganizer.databinding.ActivityRsvpTrackingBinding

class RSVPTrackingActivity : AppCompatActivity() {

    private lateinit var guestList: MutableList<Guest>
    private lateinit var guestAdapter: GuestAdapter
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityRsvpTrackingBinding
    private lateinit var eventId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRsvpTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve eventId from Intent
        eventId = intent.getStringExtra("eventId") ?: ""

        // Initialize guest list and RecyclerView
        guestList = mutableListOf()

        // Pass eventId to the adapter constructor
        guestAdapter = GuestAdapter(guestList, eventId)

        binding.guestListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.guestListRecyclerView.adapter = guestAdapter

        // Firebase database reference for guests
        database = FirebaseDatabase.getInstance().reference.child("events").child(eventId).child("guests")

        // Load guest list from Firebase
        loadGuestList()

        // Send reminder button functionality
        binding.sendReminderButton.setOnClickListener {
            sendReminders()
        }
    }

    private fun loadGuestList() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                guestList.clear()
                for (dataSnapshot in snapshot.children) {
                    val guest = dataSnapshot.getValue(Guest::class.java)
                    guest?.let { guestList.add(it) }
                }
                guestAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RSVPTrackingActivity, "Failed to load guest list", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendReminders() {
        // Check for guests with RSVP status "Pending"
        val pendingGuests = guestList.filter { it.rsvpStatus == "Pending" }

        if (pendingGuests.isNotEmpty()) {
            // Send reminder notifications or emails to pending guests
            pendingGuests.forEach {
                // You can implement Firebase Cloud Messaging (FCM) or email functionality here
                // Example: sendReminderToGuest(it)
            }
            Toast.makeText(this, "Reminders sent to ${pendingGuests.size} guests.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No pending RSVPs to remind.", Toast.LENGTH_SHORT).show()
        }
    }
}
