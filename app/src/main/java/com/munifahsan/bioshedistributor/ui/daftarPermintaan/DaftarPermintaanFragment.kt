package com.munifahsan.bioshedistributor.ui.daftarPermintaan

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.munifahsan.bioshedistributor.R
import com.munifahsan.bioshedistributor.databinding.FragmentDaftarPermintaanBinding
import com.munifahsan.bioshedistributor.models.RequestProduk
import com.munifahsan.bioshedistributor.ui.detailPermintaan.DetailPermintaanActivity
import com.munifahsan.bioshedistributor.utils.Constants
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class DaftarPermintaanFragment : Fragment() {
    private var _binding: FragmentDaftarPermintaanBinding? = null
    private val binding get() = _binding!!

    lateinit var mAnimator: ValueAnimator
    private var adapter: DaftarFirestoreRecyclerAdapter? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarPermintaanBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        showItem()
        return view
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    private fun showItem() {
        binding.rvDaftar.layoutManager = LinearLayoutManager(
            activity
        )
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("REQUEST_PRODUK").whereEqualTo("distributorId", auth.uid.toString())
        val options = FirestoreRecyclerOptions
            .Builder<RequestProduk>()
            .setQuery(query, RequestProduk::class.java)
            .build()
        adapter = DaftarFirestoreRecyclerAdapter(options)
        binding.rvDaftar.adapter = adapter
    }

    private inner class DaftarViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ), ViewTreeObserver.OnPreDrawListener {

        fun setProduct(
            permintaanId: String,
            produkId: String,
            requestDate: Date?,
            jumlahPermintaan: Int,
            diproses: Boolean,
            dikirim: Boolean,
            diterima: Boolean
        ) {
            val productName = view.findViewById<TextView>(R.id.productName)
            val idPermintaan = view.findViewById<TextView>(R.id.idPermintaan)
            val idPermintaanShimmer =
                view.findViewById<ShimmerFrameLayout>(R.id.idPermintaanShimmer)
            val tanggal = view.findViewById<TextView>(R.id.tanggalPermintaan)
            val image = view.findViewById<ImageView>(R.id.thumbnailImage)
            val jumlah = view.findViewById<TextView>(R.id.jumlahPermintaan)
            val statusTxt = view.findViewById<TextView>(R.id.status)
            val statusCard = view.findViewById<CardView>(R.id.statusCard)
            val permintaanCard = view.findViewById<CardView>(R.id.permintaanCard)

            Constants.PRODUCT_DB.document(produkId).get().addOnSuccessListener {
                if (it.exists()) {
                    productName.text = it.getString("nama")
                    if (it.getString("thumbnail") != "") {
                        Picasso.get()
                            .load(it.getString("thumbnail"))
                            .placeholder(R.drawable.black_transparent)
                            .into(image)
                    }
                }
            }

            jumlah.text = jumlahPermintaan.toString()

            tanggal.text = getTimeDate(requestDate)

            idPermintaan.text = permintaanId
            idPermintaan.visibility = View.VISIBLE
            idPermintaanShimmer.visibility = View.INVISIBLE

            if (!diproses && !dikirim && !diterima) {
                statusTxt.text = "MENUNGGU"
                statusCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        activity!!,
                        R.color.grey
                    )
                )
                statusTxt.setTextColor(ContextCompat.getColor(activity!!, R.color.black))
            } else if (diproses && !dikirim && !diterima) {
                statusTxt.text = "DIPROSES"
                statusCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        activity!!,
                        R.color.biru_muda2
                    )
                )
                statusTxt.setTextColor(ContextCompat.getColor(activity!!, R.color.biru_dasar))
            } else if (diproses && dikirim && !diterima) {
                statusTxt.text = "DIKIRIM"
                statusCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        activity!!,
                        R.color.biru_muda2
                    )
                )
                statusTxt.setTextColor(ContextCompat.getColor(activity!!, R.color.biru_dasar))
            } else if (diproses && dikirim && diterima) {
                statusTxt.text = "SELESAI"
                statusCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        activity!!,
                        R.color.biru_muda2
                    )
                )
                statusTxt.setTextColor(ContextCompat.getColor(activity!!, R.color.biru_dasar))
            }

            permintaanCard.setOnClickListener {
                val intent = Intent(activity, DetailPermintaanActivity::class.java)
                intent.putExtra("PERMINTAAN_ID", permintaanId)
                startActivity(intent)
            }
        }

        private fun expand() {
            val finalHeight: Int = view.findViewById<RelativeLayout>(R.id.expandMenu).height
            val mAnimator: ValueAnimator = slideAnimator(0, 150)
            mAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animator: Animator) {
                    //Height=0, but it set visibility to GONE

                }

                override fun onAnimationStart(animator: Animator) {
                    view.findViewById<RelativeLayout>(R.id.expandMenu).visibility = View.VISIBLE
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })

            /* Remove and used in preDrawListener
            final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            mLinearLayout.measure(widthSpec, heightSpec);
            mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
            */
            mAnimator.start()
        }

        private fun collapse() {
            val finalHeight: Int = view.findViewById<RelativeLayout>(R.id.expandMenu).height
            val mAnimator: ValueAnimator = slideAnimator(finalHeight, 0)
            mAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animator: Animator) {
                    //Height=0, but it set visibility to GONE
                    view.findViewById<RelativeLayout>(R.id.expandMenu).visibility = View.GONE
                }

                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            mAnimator.start()
        }

        private fun slideAnimator(start: Int, end: Int): ValueAnimator {
            val animator: ValueAnimator = ValueAnimator.ofInt(start, end)
            animator.addUpdateListener { valueAnimator -> //Update Height
                val value: Int = valueAnimator.animatedValue as Int
                val layoutParams: ViewGroup.LayoutParams =
                    view.findViewById<RelativeLayout>(R.id.expandMenu).layoutParams
                layoutParams.height = value
                view.findViewById<RelativeLayout>(R.id.expandMenu).layoutParams = layoutParams
            }
            return animator
        }

        override fun onPreDraw(): Boolean {
            view.findViewById<RelativeLayout>(R.id.expandMenu).viewTreeObserver.removeOnPreDrawListener(
                this
            )
            view.findViewById<RelativeLayout>(R.id.expandMenu).visibility = View.GONE

            val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.findViewById<RelativeLayout>(R.id.expandMenu).measure(widthSpec, heightSpec)

            mAnimator =
                slideAnimator(0, view.findViewById<RelativeLayout>(R.id.expandMenu).measuredHeight);
            return true;
        }
    }

    private inner class DaftarFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<RequestProduk>) :
        FirestoreRecyclerAdapter<RequestProduk, DaftarViewHolder>(
            options
        ) {
        override fun onBindViewHolder(
            daftarViewHolder: DaftarViewHolder,
            position: Int,
            model: RequestProduk
        ) {
            daftarViewHolder.setProduct(
                model.id,
                model.idProdukRequest,
                model.requestDate,
                model.jumlahRequest,
                model.diproses,
                model.dikirim,
                model.selesaiDiterima
            )

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaftarViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_permintaan_produk,
                parent,
                false
            )
            return DaftarViewHolder(view)
        }
    }

    private fun showDeleteDialog(produkId: String) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Hapus produk")
        builder.setMessage("Yakin ingin menghapus produk ini ?")
        builder.setPositiveButton("Ya") { _, _ ->
            Constants.PRODUCT_DB.document(produkId).delete().addOnSuccessListener {
                showMessage("Produk berhasil dihapus")
            }
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
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

    private fun showMessage(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DaftarPermintaanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DaftarPermintaanFragment().apply {

            }
    }
}