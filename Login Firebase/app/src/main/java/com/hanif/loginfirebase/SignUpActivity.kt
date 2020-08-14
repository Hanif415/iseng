package com.hanif.loginfirebase

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        btnSignIn.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }

        btnSignUp.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        var email: String = edtEmail.text.toString().trim()
        var password: String = edtPassword.text.toString().trim()

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password", Toast.LENGTH_SHORT).show()
            return;
        }

        if(password.length < 6) {
            Toast.makeText(this@SignUpActivity, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
            return;
        }

        progressBar!!.visibility = View.VISIBLE

//        create user
        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@SignUpActivity
            ) { task ->
                Toast.makeText(
                    this@SignUpActivity,
                    "createUserWithEmail:onComplete:" + task.isSuccessful,
                    Toast.LENGTH_SHORT
                ).show()
                progressBar!!.visibility = View.GONE
                if (!task.isSuccessful) {
                    Toast.makeText(
                        this@SignUpActivity, "Authentication failed." + task.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    finish()
                }
            }
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }
}
