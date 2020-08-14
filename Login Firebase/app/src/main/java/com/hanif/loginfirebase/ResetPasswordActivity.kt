package com.hanif.loginfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_login.progressBar as progressBar1

class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        auth = FirebaseAuth.getInstance()

        btn_back.setOnClickListener(this)
        btn_reset_password.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.btn_back -> {
                finish()
            }

            R.id.btn_reset_password -> {
                var email: String = edt_email.text.toString().trim()

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(application, "Enter you registered email", Toast.LENGTH_SHORT).show()
                    return
                }

                progressBar.visibility = View.VISIBLE
                auth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                "We have sent you instructions to reset your password!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                "Failed to send reset email!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        progressBar.visibility = View.GONE
                    }
            }
        }
    }
}
