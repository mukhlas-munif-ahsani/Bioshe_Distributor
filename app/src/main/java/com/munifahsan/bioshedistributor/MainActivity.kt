package com.munifahsan.bioshedistributor

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.munifahsan.biosheadmin.utils.CheckConection
import com.munifahsan.bioshedistributor.databinding.ActivityMainBinding
import com.munifahsan.bioshedistributor.models.RequestProduk
import com.munifahsan.bioshedistributor.ui.pageChat.ChatFragment
import com.munifahsan.bioshedistributor.ui.pageHome.HomeFragment
import com.munifahsan.bioshedistributor.ui.pagePenjualan.PenjualanFragment
import com.munifahsan.bioshedistributor.ui.pageProduk.ProdukFragment
import com.munifahsan.bioshedistributor.utils.Constants
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var mBottomSheetMintaProdukBehavior: BottomSheetBehavior<*>? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var mainHandler: Handler
    var isConected = false
    private val updateTextTask = object : Runnable {
        override fun run() {
            if (CheckConection.isNetworkAvailable(this@MainActivity)) {
                binding.ofline.visibility = View.GONE
                isConected = true
                changeNotifBarColor(this@MainActivity, R.color.biru_dasar)
            } else {
                binding.ofline.visibility = View.VISIBLE
                isConected = false
                changeNotifBarColor(this@MainActivity, R.color.black)
            }
            mainHandler.postDelayed(this, 2000)
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.itemHome -> {
                    val fragment = HomeFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemProduk -> {
                    val fragment = ProdukFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemChat -> {
                    val fragment = ChatFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemPenjualan -> {
                    val fragment = PenjualanFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemLiannya -> {
                    val fragment = ChatFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
            }

            false
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bot_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)

        val fragment = HomeFragment.newInstance()
        addFragment(fragment)

        //Bottom sheet edit harga
        val bottomSheetMintaProduk = findViewById<FrameLayout>(R.id.mintaProdukBottomSheet)
        mBottomSheetMintaProdukBehavior = BottomSheetBehavior.from(bottomSheetMintaProduk)
        (mBottomSheetMintaProdukBehavior as BottomSheetBehavior<*>).isDraggable = false

        findViewById<CardView>(R.id.close).setOnClickListener {
            (mBottomSheetMintaProdukBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
            binding.blackBg.visibility = View.GONE
        }

        binding.blackBg.setOnClickListener {
            (mBottomSheetMintaProdukBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
            binding.blackBg.visibility = View.GONE
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

    private fun showMessage(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    public fun openMintaProduk(idProduk: String, namaProduk: String) {
        (mBottomSheetMintaProdukBehavior as BottomSheetBehavior<*>).state =
            BottomSheetBehavior.STATE_EXPANDED
        binding.blackBg.visibility = View.VISIBLE
        val nama = findViewById<TextView>(R.id.namaProduk)
        nama.text = namaProduk

        val increase = findViewById<CardView>(R.id.add)
        val decrease = findViewById<CardView>(R.id.remove)
        val kirimPermintaan = findViewById<CardView>(R.id.kirimPermintaan)
        val jmlStok = findViewById<TextInputEditText>(R.id.jumlahStok)
        jmlStok.setText("0")

        increase.setOnClickListener {
            val jml = jmlStok.text.toString()
            val jmlInt = jml.toInt().plus(1)
            jmlStok.setText(jmlInt.toString())
        }

        decrease.setOnClickListener {
            val jml = jmlStok.text.toString()
            if (jml.toInt() > 0) {
                val jmlInt = jml.toInt().minus(1)
                jmlStok.setText(jmlInt.toString())
            }
        }

        kirimPermintaan.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Anda akan mengirim permintaan ${jmlStok.text.toString()} Stok untuk Produk $namaProduk ... ")
            builder.setPositiveButton("Ya") { _, _ ->
                hideKeyboard()
                Constants.DISTRIBUTOR_DB.document(auth.uid.toString()).get().addOnSuccessListener {
                    if (it.exists()) {
                        val requestData = RequestProduk(
                            "",
                            idProduk,
                            jmlStok.text.toString().toInt(),
                            0,
                            Date(),
                            auth.uid.toString(),
                            it.getString("alamat").toString(),
                            false,
                            null,
                            false,
                            null,
                            false,
                            null,
                            true,
                            "",
                            "",
                            ""
                        )
                        Constants.REQUEST_PRODUCT_DB.document().set(requestData).addOnSuccessListener {
                            (mBottomSheetMintaProdukBehavior as BottomSheetBehavior<*>).state =
                                BottomSheetBehavior.STATE_COLLAPSED
                            binding.blackBg.visibility = View.INVISIBLE
                            showMessage("Permintaan dikirim")
                        }
                    }
                }
            }
            builder.setNegativeButton("Batal") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }
    }

    override fun onBackPressed() {
        if ((mBottomSheetMintaProdukBehavior as BottomSheetBehavior<*>).state ==
            BottomSheetBehavior.STATE_EXPANDED
        ) {
            (mBottomSheetMintaProdukBehavior as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
            binding.blackBg.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.mtrl_bottom_sheet_slide_in,
                R.anim.mtrl_bottom_sheet_slide_out
            )
            .replace(R.id.fl_container, fragment, fragment.javaClass.simpleName)
            .commit()
    }

}