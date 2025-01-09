package com.shreya.eventorganizer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class InvitationActivity : AppCompatActivity() {

    private lateinit var guestRecyclerView: RecyclerView
    private lateinit var invitationMessageEditText: EditText
    private lateinit var sendInvitationsButton: Button
    private lateinit var guestAdapter: GuestAdapter
    private lateinit var guests: List<Guest>  // List of guests to invite

    private lateinit var eventId: String  // Event ID passed from DashboardActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitation)

        // Initialize views
        guestRecyclerView = findViewById(R.id.guest_list_recycler_view)
        invitationMessageEditText = findViewById(R.id.invitation_message)
        sendInvitationsButton = findViewById(R.id.send_invitations_button)

        // Get eventId from Intent
        eventId = intent.getStringExtra("eventId") ?: ""

        // Set up RecyclerView for guest list
        guestRecyclerView.layoutManager = LinearLayoutManager(this)
        guestAdapter = GuestAdapter(mutableListOf(), eventId)  // Pass eventId here
        guestRecyclerView.adapter = guestAdapter

        // Load guests from Firebase
        loadGuests()

        // Handle send invitations button click
        sendInvitationsButton.setOnClickListener {
            sendInvitations()
        }
    }

    private fun loadGuests() {
        // Example: Load guests from Firebase based on the eventId
        val database = FirebaseDatabase.getInstance().getReference("events").child(eventId).child("guests")
        database.get().addOnSuccessListener { snapshot ->
            val guestList = mutableListOf<Guest>()
            for (guestSnapshot in snapshot.children) {
                val guest = guestSnapshot.getValue(Guest::class.java)
                guest?.let { guestList.add(it) }
            }
            guests = guestList
            guestAdapter.updateGuestList(guestList)
        }
    }

    private fun sendInvitations() {
        val message = invitationMessageEditText.text.toString().trim()

        if (message.isEmpty()) {
            Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show()
            return
        }

        // Send invitations only to selected guests
        val selectedGuests = guestAdapter.getSelectedGuests()
        if (selectedGuests.isEmpty()) {
            Toast.makeText(this, "Please select at least one guest", Toast.LENGTH_SHORT).show()
            return
        }

        for (guest in guests) {
            if (selectedGuests.contains(guest.id)) {
                // Send invitation to each selected guest (via email or SMS)
                sendEmailInvitation(guest, message)
                sendSMSInvitation(guest, message)

                // Update Firebase with invitation status
                updateInvitationStatus(guest, true)
            }
        }

        Toast.makeText(this, "Invitations sent successfully", Toast.LENGTH_SHORT).show()
    }

    private fun sendEmailInvitation(guest: Guest, message: String) {
        // Example of sending email (you can use Firebase or an email API like SendGrid)
        val email = guest.email
        // Email sending logic here (Firebase or third-party service)
    }

    private fun sendSMSInvitation(guest: Guest, message: String) {
        // Example of sending SMS (e.g., using Twilio or other SMS API)
        val phoneNumber = guest.phoneNumber
        // SMS sending logic here (Twilio or other SMS API)
    }

    private fun updateInvitationStatus(guest: Guest, sent: Boolean) {
        val guestRef = FirebaseDatabase.getInstance().getReference("events")
            .child(eventId).child("guests").child(guest.id)
        guestRef.child("invitationStatus").setValue(if (sent) "Sent" else "Not Sent")
    }
}
