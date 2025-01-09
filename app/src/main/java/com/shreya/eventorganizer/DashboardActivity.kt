package com.shreya.eventorganizer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shreya.eventorganizer.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var eventList: MutableList<Event>
    private lateinit var eventAdapter: EventAdapter
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var userName: String
    private lateinit var manageGuestsButton: Button
    private lateinit var sendInvitationsButton: Button
    private lateinit var chatButton: Button
    private lateinit var settingsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manageGuestsButton = findViewById(R.id.btn_manage_guests)
        sendInvitationsButton = findViewById(R.id.open_invitation_activity_button)
        chatButton = findViewById(R.id.chatButton)
        settingsButton = findViewById(R.id.settings_button)

        userName = intent.getStringExtra("USER_NAME") ?: "User"

        val welcomeMessage = "Welcome, $userName!"
        findViewById<TextView>(R.id.welcomeMessageTextView).text = welcomeMessage

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId == null) {
            redirectToLogin()
            return
        }

        eventList = mutableListOf()
        eventAdapter = EventAdapter(eventList) { event: Event -> deleteEvent(event) }

        binding.eventRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.eventRecyclerView.adapter = eventAdapter

        binding.fabAddEvent.setOnClickListener {
            startActivity(Intent(this, EventCreationActivity::class.java))
        }

        manageGuestsButton.setOnClickListener {
            val eventId = "your_event_id_here"
            val intent = Intent(this, GuestManagementActivity::class.java)
            intent.putExtra("eventId", eventId)
            startActivity(intent)
        }

        sendInvitationsButton.setOnClickListener {
            val eventId = "your_event_id_here"
            val intent = Intent(this, InvitationActivity::class.java)
            intent.putExtra("eventId", eventId)
            startActivity(intent)
        }

        chatButton.setOnClickListener {
            val eventId = "your_event_id_here"
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("EVENT_ID", eventId)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        loadInvitationsFragment()
        loadEvents()
    }

    private fun loadInvitationsFragment() {
        val fragment = InvitationsFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        binding.fragmentContainer?.let {
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
        } ?: run {
            Toast.makeText(this, "Fragment container is missing in layout", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadEvents() {
        val database = FirebaseDatabase.getInstance().reference.child("events")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                for (dataSnapshot in snapshot.children) {
                    val event = dataSnapshot.getValue(Event::class.java) ?: continue
                    eventList.add(event)
                }
                eventList.reverse()
                eventAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DashboardActivity, "Failed to load events", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteEvent(event: Event) {
        val eventRef = FirebaseDatabase.getInstance().reference.child("events").child(event.id)

        eventRef.removeValue().addOnSuccessListener {
            eventList.remove(event)
            eventAdapter.notifyDataSetChanged()
            Toast.makeText(this, "Event deleted successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to delete event", Toast.LENGTH_SHORT).show()
        }
    }

    private fun redirectToLogin() {
        Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                auth.signOut()
                redirectToLogin()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
