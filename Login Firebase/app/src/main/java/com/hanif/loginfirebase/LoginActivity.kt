package com.hanif.loginfirebase

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        if (auth!!.currentUser != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        setSupportActionBar(toolbar)

        btnSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        btnResetPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))
        }

        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        var email: String = edtEmail.text.toString().trim()
        val password: String = edtPassword.text.toString().trim()

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password", Toast.LENGTH_SHORT).show()
            return;
        }

        progressBar.visibility = View.VISIBLE

        auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(){task ->
                progressBar.visibility = View.GONE
                if (!task.isSuccessful){
                    if (password.length < 6) {
                        edtPassword.error = getString(R.string.minimum_password)
                    } else {
                        Toast.makeText(this@LoginActivity, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show()
                    }
                }else{
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }
}
