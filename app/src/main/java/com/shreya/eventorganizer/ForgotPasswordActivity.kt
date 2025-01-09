package com.shreya.eventorganizer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password_activity)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnResetPassword = findViewById<Button>(R.id.btnResetPassword)
        val tvMessage = findViewById<TextView>(R.id.tvMessage)

        // Handle reset password button click
        btnResetPassword.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "Email address is required"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            // Send password reset email
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    tvMessage.text = "A password reset link has been sent to $email."
                    tvMessage.visibility = TextView.VISIBLE
                } else {
                    tvMessage.text = "Failed to send reset email. Please try again."
                    tvMessage.visibility = TextView.VISIBLE
                }
            }.addOnFailureListener { exception ->
                // Handle failure case and show error message
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
