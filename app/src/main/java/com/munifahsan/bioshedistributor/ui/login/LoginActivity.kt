package com.munifahsan.bioshedistributor.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.munifahsan.bioshedistributor.utils.Constants
import com.munifahsan.bioshedistributor.MainActivity
import com.munifahsan.bioshedistributor.databinding.ActivityLoginBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        binding.masukBtn.setOnClickListener {
            login()
        }

        binding.goToRegister.setOnClickListener {
            //startRegisterActivity()
        }

        setContentView(view)
    }

    private fun login() {
        val email = binding.loginEmailEdt.text.toString()
        val pass = binding.loginPassEdt.text.toString()
        if (isValidForm(email, pass)) {
            binding.loginProgressRel.visibility = View.VISIBLE
            binding.loginProgressTitle.text = "Mengecek akun"

            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Constants.DISTRIBUTOR_DB.document(FirebaseAuth.getInstance().currentUser!!.uid)
                            .get().addOnSuccessListener { result ->
                                if (result.exists()) {
                                    startActivity(Intent(this, MainActivity::class.java))
                                } else {
                                    showMessage("Data pengguna tidak ditemukan")
                                    binding.loginProgressRel.visibility = View.GONE
                                }
                            }
                    } else {
                        showMessage(it.exception!!.message.toString())
                        binding.loginProgressRel.visibility = View.GONE
                    }
                }.addOnFailureListener {
                    showMessage("Failur : ${it.message}")
                    binding.loginProgressRel.visibility = View.GONE
                }
        }
    }

    private fun isValidForm(email: String, pass: String): Boolean {
        var isValid = true
        if (email.isEmpty()) {
            isValid = false
            binding.loginEmailInLay.error = "Email tidak boleh kosong"
        }

        if (email.isNotEmpty() && !isEmailValid(email)) {
            isValid = false
            binding.loginEmailInLay.error = "Email tidak valid"
        }

        if (email.isNotEmpty()) {
            binding.loginEmailInLay.isErrorEnabled = false
        }

        if (pass.isEmpty()) {
            isValid = false
            binding.loginPassInLay.error = "Password tidak boleh kosong"
        }

        if (pass.isNotEmpty()) {
            binding.loginPassInLay.isErrorEnabled = false
        }
        return isValid
    }

    private fun isEmailValid(email: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}