package com.munifahsan.bioshedistributor.ui.splashScreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.munifahsan.bioshedistributor.utils.Constants
import com.munifahsan.bioshedistributor.MainActivity
import com.munifahsan.bioshedistributor.R
import com.munifahsan.bioshedistributor.databinding.ActivitySplashScreenBinding
import com.munifahsan.bioshedistributor.ui.isiDataDiri.IsiDataDiriActivity
import com.munifahsan.bioshedistributor.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        changeNotifBarColor(this, R.color.white)
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val userid: String? = currentUser?.uid
        if (currentUser != null) {
            currentUser.getIdToken(true).addOnCompleteListener {
//                if (it.isSuccessful){
//                    Constants.USER_DB.
//                }
            }
            Constants.DISTRIBUTOR_DB.document(userid.toString()).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        //showMessage("Data pengguna tidak ditemukan!!!")
                        startActivity(Intent(this, IsiDataDiriActivity::class.java))
                        finish()
                    }
                }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun changeNotifBarColor(context: Context, color: Int) {
        /*
        Change status bar color
        */
        val window: Window = window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(context, color)
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}