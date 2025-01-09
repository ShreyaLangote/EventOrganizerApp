package com.shreya.eventorganizer

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class GuestManagementActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var guestRecyclerView: RecyclerView
    private lateinit var guestAdapter: GuestAdapter
    private lateinit var addGuestButton: Button
    private lateinit var eventId: String // This would come from the previous activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_management)

        // Initialize UI components
        guestRecyclerView = findViewById(R.id.guest_list_recycler_view)
        addGuestButton = findViewById(R.id.add_guest_button)

        // Get eventId passed from the previous activity (Intent)
        eventId = intent.getStringExtra("eventId") ?: ""

        // Set up Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("events").child(eventId).child("guests")

        // Set up RecyclerView
        guestRecyclerView.layoutManager = LinearLayoutManager(this)
        guestAdapter = GuestAdapter(mutableListOf(), eventId)  // Pass eventId here
        guestRecyclerView.adapter = guestAdapter

        // Load guests from Firebase
        loadGuests()

        // Add Guest Button click listener
        addGuestButton.setOnClickListener {
            // Pass the Firebase reference to the dialog
            val dialog = AddGuestDialog(database)
            dialog.show(supportFragmentManager, "AddGuestDialog")
        }
    }

    // Load the list of guests from Firebase
    private fun loadGuests() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val guestList = mutableListOf<Guest>()
                for (guestSnapshot in snapshot.children) {
                    val guest = guestSnapshot.getValue(Guest::class.java)
                    guest?.let { guestList.add(it) }
                }
                guestAdapter.updateGuestList(guestList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
