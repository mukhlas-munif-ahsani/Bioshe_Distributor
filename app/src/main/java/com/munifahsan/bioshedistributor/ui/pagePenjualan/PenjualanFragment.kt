package com.munifahsan.bioshedistributor.ui.pagePenjualan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.munifahsan.bioshedistributor.R
import com.munifahsan.bioshedistributor.databinding.FragmentPenjualanBinding
import com.munifahsan.bioshedistributor.ui.daftarOrderDikirim.DaftarOrderDikirimFragment
import com.munifahsan.bioshedistributor.ui.daftarOrderDiproses.DaftarOrderDiprosesFragment
import com.munifahsan.bioshedistributor.ui.daftarOrderMenunggu.DaftarOrderMenungguFragment
import com.munifahsan.bioshedistributor.ui.daftarOrderSelesai.DaftarOrderSelesaiFragment
import com.munifahsan.bioshedistributor.ui.daftarOrderTerkirim.DaftarOrderTerkirimFragment
import com.munifahsan.bioshedistributor.ui.pageProduk.ProdukFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class PenjualanFragment : Fragment() {

    private var _binding: FragmentPenjualanBinding? = null
    private val binding get() =_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPenjualanBinding.inflate(inflater, container, false)
        val view =binding.root

        val adapter = FragmentPagerItemAdapter(
            childFragmentManager, FragmentPagerItems.with(activity)
                .add("MENUNGGU", DaftarOrderMenungguFragment::class.java)
                .add("DIPROSES", DaftarOrderDiprosesFragment::class.java)
                .add("DIKIRIM", DaftarOrderDikirimFragment::class.java)
                .add("TERKIRIM", DaftarOrderTerkirimFragment::class.java)
                .add("SELESAI", DaftarOrderSelesaiFragment::class.java)
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

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): PenjualanFragment {
            val fragment = PenjualanFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}