package com.shreya.eventorganizer

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var checkboxEventNotifications: CheckBox
    private lateinit var checkboxInvitationNotifications: CheckBox
    private lateinit var btnSaveChanges: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        editName = findViewById(R.id.edit_name)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        checkboxEventNotifications = findViewById(R.id.checkbox_event_notifications)
        checkboxInvitationNotifications = findViewById(R.id.checkbox_invitation_notifications)
        btnSaveChanges = findViewById(R.id.btn_save_changes)

        // Fetch current user's information
        fetchUserProfile()

        // Save Changes Button Click
        btnSaveChanges.setOnClickListener {
            saveProfileChanges()
            saveNotificationSettings()
        }
    }

    // Fetch current user's information and populate the fields
    private fun fetchUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            editName.setText(user.displayName)
            editEmail.setText(user.email)
            // Load notification preferences from Firebase or local storage if needed
            // checkboxEventNotifications.isChecked = ...
            // checkboxInvitationNotifications.isChecked = ...
        }
    }

    // Save profile changes (name, email, and password)
    private fun saveProfileChanges() {
        val name = editName.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val password = editPassword.text.toString().trim()

        val user = auth.currentUser
        if (user != null) {
            // Update name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Name updated successfully", Toast.LENGTH_SHORT).show()
                }
            }

            // Update email if changed
            if (email != user.email) {
                user.updateEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email updated successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Update password if provided
            if (password.isNotEmpty()) {
                user.updatePassword(password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Save notification settings (using Firebase Realtime Database or SharedPreferences)
    private fun saveNotificationSettings() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val database = FirebaseDatabase.getInstance().reference
            val notificationsRef = database.child("users").child(userId).child("notifications")

            // Store notification preferences
            val notifications = hashMapOf(
                "event_notifications" to checkboxEventNotifications.isChecked,
                "invitation_notifications" to checkboxInvitationNotifications.isChecked
            )

            notificationsRef.setValue(notifications).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Notification settings updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update notification settings", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
