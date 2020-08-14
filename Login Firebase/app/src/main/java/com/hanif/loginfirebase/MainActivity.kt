package com.hanif.loginfirebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var authListener: FirebaseAuth.AuthStateListener? = null
    var auth: FirebaseAuth? = null
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()

        user = FirebaseAuth.getInstance().currentUser!!

        authListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }

        edtOldEmail.visibility = View.GONE
        edtNewEmail.visibility = View.GONE
        edtOldPassword.visibility = View.GONE
        edtNewPassword.visibility = View.GONE
        btnChangeEmail.visibility = View.GONE
        btnChangePass.visibility = View.GONE
        btnSend.visibility = View.GONE
        btnRemove.visibility = View.GONE

        if (progressBar != null) {
            progressBar.visibility = View.GONE
        }

        btnChangeEmails.setOnClickListener(this)
        btnChangeEmail.setOnClickListener(this)

        btnChangePass.setOnClickListener(this)
        btnChangePasswords.setOnClickListener(this)

        btnSendingEmailReset.setOnClickListener(this)
        btnSend.setOnClickListener(this)

        btnRemoveUserButton.setOnClickListener(this)

        btnSignOut.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnChangeEmails -> {
                edtOldEmail.visibility = View.GONE
                edtNewEmail.visibility = View.VISIBLE
                edtOldPassword.visibility = View.GONE
                edtNewPassword.visibility = View.GONE
                btnChangeEmail.visibility = View.VISIBLE
                btnChangePass.visibility = View.GONE
                btnSend.visibility = View.GONE
                btnRemove.visibility = View.GONE
            }

            R.id.btnChangeEmail -> {
                progressBar.visibility = View.VISIBLE
                if (user != null && edtNewEmail.text.toString().trim { it <= ' ' } != "") {
                    user!!.updateEmail(edtNewEmail.text.toString().trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Email address is updated. Please sign in with new email id!",
                                    Toast.LENGTH_LONG
                                ).show()
                                signOut()
                                progressBar.visibility = View.GONE
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Failed to update email!",
                                    Toast.LENGTH_LONG
                                ).show()
                                progressBar.visibility = View.GONE
                            }
                        }
                } else if (edtNewEmail.text.toString().trim() == "") {
                    edtNewEmail.error = "Enter Email"
                    progressBar.visibility = View.GONE
                }
            }

            R.id.btnChangePasswords -> {
                edtOldEmail.visibility = View.GONE
                edtNewEmail.visibility = View.GONE
                edtOldPassword.visibility = View.GONE
                edtNewPassword.visibility = View.VISIBLE
                btnChangeEmail.visibility = View.GONE
                btnChangePass.visibility = View.VISIBLE
                btnSend.visibility = View.GONE
                btnRemove.visibility = View.GONE
            }

            R.id.btnChangePass -> {
                progressBar.visibility = View.VISIBLE
                if (user != null && edtNewPassword.text.toString().trim{it <= ' '} != "") {
                    if (edtNewPassword.text.toString().trim().length < 6) {
                        edtNewPassword.error = "Password too short, enter minimum 6 characters"
                        progressBar.visibility = View.GONE
                    } else {
                        user!!.updatePassword(edtNewPassword.text.toString().trim())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Password is updated. Please sign in with new password!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    signOut()
                                    progressBar.visibility = View.GONE
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Failed to update password!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    progressBar.visibility = View.GONE
                                }
                            }
                    }
                } else if (edtNewEmail.text.toString().trim() == "") {
                    edtNewEmail.error = "Enter Password"
                    progressBar.visibility = View.GONE
                }
            }

            R.id.btnSendingEmailReset -> {
                edtOldEmail.visibility = View.VISIBLE
                edtNewEmail.visibility = View.GONE
                edtOldPassword.visibility = View.GONE
                edtNewPassword.visibility = View.GONE
                btnChangeEmail.visibility = View.GONE
                btnChangePass.visibility = View.GONE
                btnSend.visibility = View.VISIBLE
                btnRemove.visibility = View.GONE
            }

            R.id.btnSend -> {
                progressBar.visibility = View.VISIBLE
                if (edtOldEmail.text.toString().trim { it <= ' ' } != "") {
                    auth!!.sendPasswordResetEmail(edtOldEmail.text.toString().trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Reset password email is sent!",
                                    Toast.LENGTH_LONG
                                ).show()
                                progressBar.visibility = View.GONE
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Failed to send reset email!",
                                    Toast.LENGTH_LONG
                                ).show()
                                progressBar.visibility = View.GONE
                            }
                        }
                } else if (edtNewEmail.text.toString().trim() == "") {
                    edtNewEmail.error = "Enter Email"
                    progressBar.visibility = View.GONE
                }
            }

            R.id.btnRemoveUserButton -> {
                progressBar.visibility = View.VISIBLE
                if (user != null) {
                    user!!.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Your profile is deleted:( Create a account now!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        SignUpActivity::class.java
                                    )
                                )
                                finish()
                                progressBar.visibility = View.GONE
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Failed to delete your account!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar.visibility = View.GONE
                            }
                        }
                }
            }

            R.id.btnSignOut -> {
                signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    //sign out method
    private fun signOut() {
        auth!!.signOut()
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.GONE
    }

    override fun onRestart() {
        super.onRestart()
        auth!!.addAuthStateListener(authListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }
}
