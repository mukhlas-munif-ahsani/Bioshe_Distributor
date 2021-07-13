package com.munifahsan.bioshedistributor.ui.mintaProduk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.munifahsan.bioshedistributor.databinding.ActivityMintaProdukBinding

class MintaProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMintaProdukBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMintaProdukBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
    }
}