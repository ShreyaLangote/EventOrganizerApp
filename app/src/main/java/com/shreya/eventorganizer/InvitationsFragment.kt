package com.shreya.eventorganizer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shreya.eventorganizer.adapters.InvitationAdapter
import com.shreya.eventorganizer.models.Invitation

class InvitationsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var invitationsAdapter: InvitationAdapter
    private lateinit var database: DatabaseReference
    private val invitationsList = mutableListOf<Invitation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_invitations, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rvInvitations)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        invitationsAdapter = InvitationAdapter(invitationsList)
        recyclerView.adapter = invitationsAdapter

        // Initialize Firebase Database Reference
        database = FirebaseDatabase.getInstance().getReference("invitations")

        // Fetch invitations
        fetchInvitations()

        return view
    }

    private fun fetchInvitations() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val invitationsRef = database.child(currentUserUid)

        invitationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                invitationsList.clear()

                snapshot.children.forEach { invitationSnapshot ->
                    val invitation = invitationSnapshot.getValue(Invitation::class.java)
                    invitation?.let { invitationsList.add(it) }
                }

                // Update RecyclerView or UI with invitations
                updateInvitationsUI(invitationsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchInvitations", "Error fetching invitations: ${error.message}")
                Toast.makeText(requireContext(), "Error fetching invitations", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateInvitationsUI(invitationsList: List<Invitation>) {
        invitationsAdapter.updateInvitations(invitationsList) // Update the adapter for RecyclerView
    }
}
