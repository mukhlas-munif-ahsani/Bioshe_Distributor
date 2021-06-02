package com.munifahsan.bioshedistributor.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.munifahsan.biosheadmin.utils.Constants
import com.munifahsan.bioshedistributor.MainActivity
import com.munifahsan.bioshedistributor.databinding.ActivitySplashScreenBinding
import com.munifahsan.bioshedistributor.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val userid: String? = currentUser?.uid
        if (currentUser != null){
            currentUser.getIdToken(true).addOnCompleteListener {
//                if (it.isSuccessful){
//                    Constants.USER_DB.
//                }
            }
            Constants.DISTRIBUTOR_DB.document(userid.toString()).get().addOnSuccessListener {
                if (it.exists()){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    //showMessage("Data pengguna tidak ditemukan!!!")
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}