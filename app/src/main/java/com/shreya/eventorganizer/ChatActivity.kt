package com.shreya.eventorganizer

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var messagesAdapter: MessageAdapter
    private lateinit var messagesList: MutableList<Message>
    private lateinit var database: DatabaseReference
    private lateinit var usersRef: DatabaseReference

    private val eventId = "event123" // Replace with actual event ID
    private lateinit var currentUserUid: String
    private var currentUserName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Initialize views
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        // Initialize list and adapter for messages
        messagesList = mutableListOf()
        messagesAdapter = MessageAdapter(messagesList)

        // Set RecyclerView properties
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = messagesAdapter

        // Firebase references
        database = FirebaseDatabase.getInstance().getReference("events").child(eventId).child("messages")
        usersRef = FirebaseDatabase.getInstance().getReference("users")

        // Get current user UID
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            currentUserUid = user.uid
            usersRef.child(currentUserUid).child("username").get()
                .addOnSuccessListener { snapshot ->
                    currentUserName = snapshot.value.toString()
                }
                .addOnFailureListener { Log.e("ChatActivity", "Failed to fetch current username.") }
        } else {
            Log.e("ChatActivity", "User not authenticated")
            return
        }

        // Fetch messages
        fetchMessages()

        // Handle send button click
        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun fetchMessages() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                val usersRef = FirebaseDatabase.getInstance().getReference("users")

                snapshot.children.forEach { messageSnapshot ->
                    val message = messageSnapshot.getValue(Message::class.java)
                    message?.let {
                        // Fetch email for the sender's UID
                        usersRef.child(it.sender).child("email").get()
                            .addOnSuccessListener { emailSnapshot ->
                                val email = emailSnapshot.value.toString()
                                it.sender = email // Replace UID with email
                                messagesList.add(it)
                                messagesAdapter.notifyDataSetChanged()
                            }
                            .addOnFailureListener { error ->
                                Log.e("ChatActivity", "Error fetching email: ${error.message}")
                            }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatActivity", "Error fetching messages: ${error.message}")
            }
        })
    }

    private fun sendMessage() {
        val messageText = messageInput.text.toString().trim()
        if (messageText.isNotEmpty()) {
            // Create a message object with the current user's UID
            val message = Message(sender = currentUserUid, message = messageText, timestamp = System.currentTimeMillis())

            // Push message to Firebase and clear the input field
            val messageId = database.push().key ?: return
            database.child(messageId).setValue(message).addOnSuccessListener {
                messageInput.text.clear() // Clear input after sending the message
            }.addOnFailureListener { error ->
                Log.e("ChatActivity", "Failed to send message: ${error.message}")
            }
        } else {
            Log.w("ChatActivity", "Cannot send an empty message")
        }
    }
}
