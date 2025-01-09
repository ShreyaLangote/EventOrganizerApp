package com.shreya.eventorganizer

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.DatabaseReference

class AddGuestDialog(private val database: DatabaseReference) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_guest)

        val nameEditText: EditText = dialog.findViewById(R.id.name_edit_text)
        val emailEditText: EditText = dialog.findViewById(R.id.email_edit_text)
        val addButton: Button = dialog.findViewById(R.id.add_button)

        addButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
                // Show error message
                return@setOnClickListener
            }

            val guestId = database.push().key ?: return@setOnClickListener
            val guest = Guest(guestId, name, email)

            // Save guest to Firebase
            database.child(guestId).setValue(guest)

            dialog.dismiss()
        }

        return dialog
    }
}
