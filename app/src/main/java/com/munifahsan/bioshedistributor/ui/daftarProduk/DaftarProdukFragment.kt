package com.munifahsan.bioshedistributor.ui.daftarProduk

import android.animation.Animator
import android.animation.ValueAnimator
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.munifahsan.biosheadmin.utils.CheckConection
import com.munifahsan.bioshedistributor.MainActivity
import com.munifahsan.bioshedistributor.R
import com.munifahsan.bioshedistributor.databinding.FragmentDaftarProdukBinding
import com.munifahsan.bioshedistributor.models.Produk
import com.munifahsan.bioshedistributor.ui.pageProduk.ProdukFragment
import com.munifahsan.bioshedistributor.utils.Constants
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DaftarProdukFragment : Fragment() {

    private var _binding: FragmentDaftarProdukBinding? = null
    private val binding get() = _binding!!

    lateinit var mAnimator: ValueAnimator
    private var adapter: ProductFirestoreRecyclerAdapter? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarProdukBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        showItem()
        // Inflate the layout for this fragment
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
        binding.rvProduk.layoutManager = LinearLayoutManager(
            activity
        )
        val rootRef = FirebaseFirestore.getInstance()
        val query =
            rootRef.collection("PRODUCT").whereEqualTo("selesai", true).whereEqualTo("show", true)
        val options = FirestoreRecyclerOptions
            .Builder<Produk>()
            .setQuery(query, Produk::class.java)
            .build()
        adapter = ProductFirestoreRecyclerAdapter(options)
        binding.rvProduk.adapter = adapter
    }

    private inner class ProductViewHolder(private val view: View) :
        RecyclerView.ViewHolder(
            view
        ), ViewTreeObserver.OnPreDrawListener {

        fun setProduct(
            itemId: String,
            promoId: String,
            namaProduct: String, thumbnailProduct: String,
            hargaProduct: Int, disconProduct: Int, show: Boolean, stok: Int
        ) {
            val productName = view.findViewById<TextView>(R.id.productName)
            val priceDiscon = view.findViewById<TextView>(R.id.priceDisconTxt)
            val disconTxt = view.findViewById<TextView>(R.id.disconTxt)
            val priceProduct = view.findViewById<TextView>(R.id.priceProduct)
            val linDiscon = view.findViewById<LinearLayout>(R.id.linDiscon)
            val image = view.findViewById<ImageView>(R.id.thumbnailImage)

            if (thumbnailProduct != "") {
                Picasso.get()
                    .load(thumbnailProduct)
                    .placeholder(R.drawable.black_transparent)
                    .into(image)
            }

            productName.text = namaProduct
            disconTxt.text = disconProduct.toString()
            priceDiscon.text = rupiahFormat(hargaProduct)
            priceDiscon.paintFlags = priceDiscon.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            if (disconProduct == 0) {
                linDiscon.visibility = View.INVISIBLE
            } else {
                linDiscon.visibility = View.VISIBLE
            }

            val disconNum: Int = disconProduct
            val disconHarga: Int = hargaProduct * disconNum / 100
            val harga = hargaProduct - disconHarga
            priceProduct.text = rupiahFormat(harga)

            if (show) {
                view.findViewById<CardView>(R.id.statusShowCard).visibility = View.VISIBLE
                view.findViewById<CardView>(R.id.statusShowCard)
                    .setCardBackgroundColor(Color.parseColor("#D7EDFE"))
                view.findViewById<TextView>(R.id.textStatus)
                    .setTextColor(Color.parseColor("#118EEA"))
                view.findViewById<TextView>(R.id.textStatus).text = "Aktif"
            } else {
                view.findViewById<CardView>(R.id.statusShowCard).visibility = View.VISIBLE
                view.findViewById<CardView>(R.id.statusShowCard)
                    .setCardBackgroundColor(Color.parseColor("#B8CCDC"))
                view.findViewById<TextView>(R.id.textStatus)
                    .setTextColor(Color.parseColor("#FF000000"))
                view.findViewById<TextView>(R.id.textStatus).text = "Nonaktif"
            }

            view.findViewById<RelativeLayout>(R.id.expandMenu).viewTreeObserver.addOnPreDrawListener(
                this
            )

            view.findViewById<CardView>(R.id.produkCard).setOnClickListener {
                if (view.findViewById<RelativeLayout>(R.id.expandMenu).visibility == View.GONE) {
                    expand()
                } else {
                    collapse()
                }
            }

            view.findViewById<CardView>(R.id.editDetail).setOnClickListener {
                if (CheckConection.isNetworkAvailable(activity!!)) {
//                    val intent = Intent(activity, EditProdukActivity::class.java)
//                    intent.putExtra("PRODUK_ID", itemId)
//                    startActivity(intent)

                    (activity as MainActivity?)!!.openMintaProduk(itemId, namaProduct)

                } else {
                    showMessage("Mohon periksa koneksi internet anda")
                }
            }

            //Jumlah stok tersedia pada gudang distributor
            Constants.DISTRIBUTOR_DB.document(auth.currentUser!!.uid)
                .collection("STOK_PRODUK").document(itemId).get().addOnSuccessListener {
                if (it.exists()) {
                    view.findViewById<TextView>(R.id.stokProduct).text =
                        "Stok : ${it.getLong("stok")}"
                } else {
                    view.findViewById<TextView>(R.id.stokProduct).text = "Stok : 0"
                }
            }


            view.findViewById<CardView>(R.id.removeItem).setOnClickListener {
                showDeleteDialog(itemId)
            }

//            if (promoId != ""){
//                Constants.PROMO_DB.document(promoId).get().addOnSuccessListener {
//                    if (it.exists()){
//                        Constants.PROMO_DB.document(promoId).addSnapshotListener { value, error ->
//                            val diskon = value!!.getLong("diskon")!!.toInt()
//                            Constants.PRODUCT_DB.document(produkId).update("diskon", diskon)
//                        }
//                    }
//                }
//            }
        }

        private fun expand() {
            //set Visible
//            view.findViewById<RelativeLayout>(R.id.expandMenu).visibility = View.VISIBLE

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

    private inner class ProductFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<Produk>) :
        FirestoreRecyclerAdapter<Produk, ProductViewHolder>(
            options
        ) {
        override fun onBindViewHolder(
            productViewHolder: ProductViewHolder,
            position: Int,
            model: Produk
        ) {
            productViewHolder.setProduct(
                model.id,
                model.promoId,
                model.nama,
                model.thumbnail,
                model.harga,
                model.diskon,
                model.show,
                model.stok
            )

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_produk,
                parent,
                false
            )
            return ProductViewHolder(view)
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DaftarProdukFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DaftarProdukFragment().apply {

            }
    }
}