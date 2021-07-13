package com.munifahsan.bioshedistributor.ui.detailPermintaan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.munifahsan.bioshedistributor.R
import com.munifahsan.bioshedistributor.databinding.ActivityDetailPermintaanBinding
import com.squareup.picasso.Picasso
import io.grpc.Context
import java.text.SimpleDateFormat
import java.util.*

class DetailPermintaanActivity : AppCompatActivity(), DetailPermintaanContract.View {
    private lateinit var binding: ActivityDetailPermintaanBinding
    private lateinit var mPres: DetailPermintaanContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPermintaanBinding.inflate(layoutInflater)
        val view = binding.root

        mPres = DetailPermintaanPresenter(this)
        val intent = intent
        val idPermintaan = intent.getStringExtra("PERMINTAAN_ID")
        mPres.getDataPermintaan(idPermintaan.toString())

        binding.confirmButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah anda yakin telah menerima Produk dengan jumlah yang sesuai ?")
            builder.setPositiveButton("ya") { _, _ ->
                mPres.updateKonfirmasiDiterima(idPermintaan.toString())
            }
            builder.setNegativeButton("Batal") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }

        binding.confirmButton.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey))
        binding.confirmButton.isEnabled = false

        binding.backIcon.setOnClickListener {
            finish()
        }

        setContentView(view)
    }

    override fun enableConfirmButton(){
        binding.confirmButton.setCardBackgroundColor(ContextCompat.getColor(this, R.color.biru_dasar))
        binding.confirmButton.isEnabled = true
    }

    override fun disableConfirmButton(){
        binding.confirmButton.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey))
        binding.confirmButton.isEnabled = false
    }

    override fun showStatus(status: String) {
        binding.status.text = status
        binding.status.visibility = View.VISIBLE
        binding.statusPesananShimmer.visibility = View.INVISIBLE
    }

    override fun hideStatus() {
        binding.status.visibility = View.INVISIBLE
        binding.statusPesananShimmer.visibility = View.VISIBLE
    }

    override fun showTanggal(tanggal: Date?) {
        binding.tanggalPermintaan.text = getTimeDate(tanggal)
        binding.tanggalPermintaan.visibility = View.VISIBLE
        binding.tanggalPermintaanShimmer.visibility = View.INVISIBLE
    }

    override fun hideTanggal() {
        binding.tanggalPermintaan.visibility = View.INVISIBLE
        binding.tanggalPermintaanShimmer.visibility = View.VISIBLE
    }

    override fun showIdPermintaan(id: String) {
        binding.idPermintaan.text = id
        binding.idPermintaan.visibility = View.VISIBLE
        binding.idPermintaanShimmer.visibility = View.INVISIBLE
    }

    override fun hideIdPermintaan() {
        binding.idPermintaan.visibility = View.INVISIBLE
        binding.idPermintaanShimmer.visibility = View.VISIBLE
    }

    override fun showProdukImage(image: String) {
        Picasso.get().load(image).into(binding.thumbnailImage)
    }

    override fun showNamaProduk(nama: String) {
        binding.productName.text = nama
        binding.productName.visibility = View.VISIBLE
    }

    override fun hideNamaProduk() {
        binding.productName.visibility = View.INVISIBLE
    }

    override fun showJumlahPermintaan(jumlah: String) {
        binding.jumlahPermintaan.text = jumlah
        binding.jumlahPermintaan.visibility = View.VISIBLE
    }

    override fun hideJumlahPermintaan() {
        binding.jumlahPermintaan.visibility = View.INVISIBLE
    }

    override fun showKurir(kurir: String) {
        binding.kurirPengirim.text = kurir
        binding.kurirPengirim.visibility = View.VISIBLE
        binding.kurirShimmer.visibility = View.INVISIBLE
    }

    override fun hideKurir() {
        binding.kurirPengirim.visibility = View.INVISIBLE
        binding.kurirShimmer.visibility = View.VISIBLE
    }

    override fun showNoResi(resi: String) {
        binding.noResi.text = resi
        binding.noResi.visibility = View.VISIBLE
        binding.noResiShimmer.visibility = View.INVISIBLE
    }

    override fun hiedNoResi() {
        binding.noResi.visibility = View.INVISIBLE
        binding.noResiShimmer.visibility = View.VISIBLE
    }

    override fun showNamaPenerima(nama: String) {
        binding.nama.text = nama
        binding.nama.visibility = View.VISIBLE
    }

    override fun hideNamaPenerima() {
        binding.nama.visibility = View.INVISIBLE
    }

    override fun showNoPenerima(no: String) {
        binding.nomor.text = no
        binding.nomor.visibility = View.VISIBLE
    }

    override fun hideNoPenerima() {
        binding.nomor.visibility = View.INVISIBLE
    }

    override fun showAlamatPengiriman(alamat: String) {
        binding.alamatPengiriman.text = alamat
        binding.alamatPengiriman.visibility = View.VISIBLE
        binding.alamatPengirimanShimmer.visibility = View.INVISIBLE
    }

    override fun hideAlamatPengiriman() {
        binding.alamatPengirimanShimmer.visibility = View.VISIBLE
        binding.alamatPengiriman.visibility = View.INVISIBLE
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
}