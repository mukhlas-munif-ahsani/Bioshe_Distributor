package com.munifahsan.bioshedistributor.ui.pageProduk

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.munifahsan.biosheadmin.utils.CheckConection
import com.munifahsan.bioshedistributor.R
import com.munifahsan.bioshedistributor.databinding.FragmentProdukBinding
import com.munifahsan.bioshedistributor.models.Produk
import com.munifahsan.bioshedistributor.ui.daftarPermintaan.DaftarPermintaanFragment
import com.munifahsan.bioshedistributor.ui.daftarProduk.DaftarProdukFragment
import com.munifahsan.bioshedistributor.ui.pageHome.HomeFragment
import com.munifahsan.bioshedistributor.utils.Constants
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ProdukFragment : Fragment() {
    private var _binding: FragmentProdukBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProdukBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        val adapter = FragmentPagerItemAdapter(
            childFragmentManager, FragmentPagerItems.with(activity)
                .add("PRODUK", DaftarProdukFragment::class.java)
                .add("PERMINTAAN", DaftarPermintaanFragment::class.java)
                .create()
        )

        binding.viewPager.adapter = adapter

        binding.viewPagerTab.setViewPager(binding.viewPager)

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
//                when (position) {
//                    0 ->{
//                        binding.toolbarTitle.text = "Daftar Produk"
//                        binding.addItemTitle.text = "Tambah Produk"
//                        pagePosition = position
//                    }
//                    1 ->{
//                        binding.toolbarTitle.text = "Daftar Promo"
//                        binding.addItemTitle.text = "Tambah Promo"
//                        pagePosition = position
//                    }
//                    2 ->{
//                        binding.toolbarTitle.text = "Daftar Reward"
//                        binding.addItemTitle.text = "Tambah Reward"
//                        pagePosition = position
//                    }
//                }
                //Toast.makeText(this@KaryawanActivity, "Position : $position", Toast.LENGTH_LONG).show()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): ProdukFragment {
            val fragment = ProdukFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}