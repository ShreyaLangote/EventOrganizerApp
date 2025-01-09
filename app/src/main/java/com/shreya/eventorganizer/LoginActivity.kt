package com.shreya.eventorganizer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var continueWithoutLoginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if the user is already logged in
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            navigateToDashboard()
            return
        }

        // Initialize UI elements
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)
        continueWithoutLoginButton = findViewById(R.id.continueWithoutLoginButton)
        val signupTextView: TextView = findViewById(R.id.signup_text_view)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword) // Forgot Password TextView

        // Handle "Login" button click
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty()) {
                emailEditText.error = "Email is required"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password is required"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Enter a valid email"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        // Handle "Continue Without Login" button click
        continueWithoutLoginButton.setOnClickListener {
            navigateToDashboard()
        }

        // Handle "Sign Up" text click
        signupTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Handle "Forgot Password" text click
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // After login is successful, get the user's name from Firebase and pass it to the DashboardActivity
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        val userId = user.uid
                        val database = FirebaseDatabase.getInstance().reference
                        database.child("users").child(userId).get().addOnSuccessListener { snapshot ->
                            val userName = snapshot.child("name").value.toString()
                            val intent = Intent(this, DashboardActivity::class.java)
                            intent.putExtra("USER_NAME", userName) // Pass the name to the next activity
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Log.e("LoginError", "Login failed: ${task.exception?.message}")
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
