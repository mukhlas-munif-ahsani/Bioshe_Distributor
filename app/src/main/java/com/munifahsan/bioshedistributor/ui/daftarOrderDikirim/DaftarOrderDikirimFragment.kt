package com.munifahsan.bioshedistributor.ui.daftarOrderDikirim

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.munifahsan.bioshedistributor.R
import com.munifahsan.bioshedistributor.databinding.FragmentDaftarOrderDikirimBinding
import com.munifahsan.bioshedistributor.databinding.FragmentDaftarOrderSelesaiBinding
import com.munifahsan.bioshedistributor.models.Orders
import com.munifahsan.bioshedistributor.ui.daftarOrderSelesai.DaftarOrderSelesaiFragment
import com.munifahsan.bioshedistributor.utils.Constants
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


class DaftarOrderDikirimFragment : Fragment() {

    private var _binding: FragmentDaftarOrderDikirimBinding? = null
    private val binding get() = _binding!!
    private var adapterTransaksi: TransaksiFirestoreRecyclerAdapter? = null
    private lateinit var auth: FirebaseAuth
    private val dbOrders = FirebaseFirestore.getInstance()
        .collection("ORDERS")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarOrderDikirimBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        showTransaksi()

        return view
    }

    override fun onStart() {
        super.onStart()
        adapterTransaksi!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapterTransaksi!!.stopListening()
    }

    private fun showTransaksi() {
        binding.rvOrder.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("ORDERS")
            .whereEqualTo("membayar", true)
            .whereEqualTo("dibayar", true)
            .whereEqualTo("diproses", true)
            .whereEqualTo("dikirim", true)
            .whereEqualTo("selesaiDikirim", false)
            .whereEqualTo( "selesaiDiterima", false)
            .orderBy("orderDate", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions
            .Builder<Orders>()
            .setQuery(query, Orders::class.java)
            .build()
        adapterTransaksi = TransaksiFirestoreRecyclerAdapter(options)
        binding.rvOrder.adapter = adapterTransaksi
    }

    private inner class ProductViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ) {

        fun setId(idOrder: String) {
            val idPesanan = view.findViewById<TextView>(R.id.idPesanan)
            val idPesananShimmer = view.findViewById<ShimmerFrameLayout>(R.id.idPesananShimmer)
            val carditem = view.findViewById<CardView>(R.id.cardItem)
            //("..." + idOrder.substring(15, idOrder.length)).also { idPesanan.text = it }

            showTotalBarangHarga(dbOrders.document(idOrder).collection("PRODUCT"))

            idPesanan.visibility = View.INVISIBLE
            idPesananShimmer.visibility = View.VISIBLE
            Constants.ORDERS_DB.document(idOrder).get().addOnSuccessListener {
                if (it.exists()){
                    Constants.USERS_DB.document(it.getString("userId").toString()).get().addOnSuccessListener { user->
                        idPesanan.visibility = View.VISIBLE
                        idPesananShimmer.visibility = View.INVISIBLE
                        "${user.getString("nama").toString()} (${user.getString("namaOutlet")})".also { idPesanan.text = it }
                    }
                }
            }

            carditem.setOnClickListener {
                //start detailPesanan activity
//                val intent = Intent(activity, DetailTransaksiActivity::class.java)
//                intent.putExtra("ORDER_ID", idOrder)
//                startActivity(intent)
            }
        }

        fun setTanggal(tanggal: Date?) {
            val tanggalPesanan = view.findViewById<TextView>(R.id.tanggalPemesanan)
            tanggalPesanan.text = getTimeDate(tanggal)
        }

        fun setStatus(status: String) {
            val statusTxt = view.findViewById<TextView>(R.id.status)
            val statusCard = view.findViewById<CardView>(R.id.statusCard)
//            if (status == "MENUNGGU PEMBAYARAN"){
//                statusCard.setCardBackgroundColor(Color.parseColor("#F8CAB6"))
//                statusTxt.setTextColor(Color.parseColor("#EA5411"))
//            } else {
//                statusCard.setCardBackgroundColor(Color.parseColor("#B7DDF9"))
//                statusTxt.setTextColor(Color.parseColor("#118EEA"))
//            }
//            statusTxt.text = status
            statusTxt.text = "DIKIRIM"
            statusCard.setCardBackgroundColor(ContextCompat.getColor(activity!!, R.color.biru_dasar))
            statusTxt.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
        }

        fun showTotalBarangHarga(ref: CollectionReference) {
            val totalBarang = view.findViewById<TextView>(R.id.totalBelanja)
            val totalBayar = view.findViewById<TextView>(R.id.totalHarga)

            ref.addSnapshotListener { value, error ->
                val jumlah = ArrayList<Int>()
                var jumlahItem = 0
                for (field in value!!) {
                    val a = field.getLong("harga")
                    val b = field.getLong("jumlahItem")
                    val c = field.getLong("diskon")
                    if (a != null && b != null && c != null){
                        jumlahItem += b.toInt()
                        val disconNum: Int = c.toInt()
                        val disconHarga: Int = a.toInt() * disconNum / 100
                        val harga = a - disconHarga
                        jumlah.add(harga.toInt() * b.toInt())
                    }
                }
                "Total Belanja (${jumlahItem} Barang)".also { totalBarang.text = it }
                totalBayar.text = rupiahFormat(jumlah.sum())
            }
        }

        fun setMetodePembayaran(metode: String){
            val metodePembayaran = view.findViewById<TextView>(R.id.metodePembayaran)
            metodePembayaran.text = metode
        }
    }

    private inner class TransaksiFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<Orders>) :
        FirestoreRecyclerAdapter<Orders, ProductViewHolder>(
            options
        ) {
        override fun onBindViewHolder(
            productViewHolder: ProductViewHolder,
            position: Int,
            ordersModel: Orders
        ) {
            productViewHolder.setId(ordersModel.id)
            productViewHolder.setTanggal(ordersModel.orderDate)
            productViewHolder.setStatus(ordersModel.orderStatus)
            productViewHolder.setMetodePembayaran(ordersModel.metodePembayaran)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_transaksi,
                parent,
                false
            )
            return ProductViewHolder(view)
        }
    }

    private fun getTimeDate(timestamp: Date?): String? {
        return try {
            //Date netDate = (timestamp);
            val sfd = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            sfd.format(timestamp)
        } catch (e: Exception) {
            "date"
        }
    }

    private fun rupiahFormat(number: Int): String {
        val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp = DecimalFormatSymbols()
        formatRp.currencySymbol = "Rp "
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'
        kursIndonesia.decimalFormatSymbols = formatRp
        val harga = kursIndonesia.format(number).toString()
        return harga.replace(",00", " ")
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DaftarOrderDikirimFragment().apply {

            }
    }
}