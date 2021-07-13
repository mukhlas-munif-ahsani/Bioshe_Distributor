package com.munifahsan.bioshedistributor.ui.detailPermintaan

import java.util.*

class DetailPermintaanPresenter(val mView: DetailPermintaanContract.View) :
    DetailPermintaanContract.Presenter, DetailPermintaanContract.Listener {
    private val mRepo: DetailPermintaanContract.Repository

    init {
        mRepo = DetailPermintaanRepository(this)
    }

    override fun getDataPermintaan(idPermintaan: String) {
        mRepo.getDataPermintaan(idPermintaan)

        mView.hideStatus()
        mView.hideTanggal()
        mView.hideIdPermintaan()
        mView.hideNamaProduk()
        mView.hideJumlahPermintaan()
        mView.hideKurir()
        mView.hiedNoResi()
        mView.hideNamaPenerima()
        mView.hideNoPenerima()
        mView.hideAlamatPengiriman()
    }

    override fun updateKonfirmasiDiterima(idPermintaan: String) {
        mRepo.updateKonfirmasiDiterima(idPermintaan)
    }

    override fun getDataStatusListener(diproses: Boolean, dikirim: Boolean, diterima: Boolean) {
        if (!diproses && !dikirim && !diterima) {
            mView.showStatus("MENUNGGU")
            mView.disableConfirmButton()
        } else if (diproses && !dikirim && !diterima) {
            mView.showStatus("DIPROSES")
            mView.disableConfirmButton()
        } else if (diproses && dikirim && !diterima) {
            mView.showStatus("DIKIRIM")
            mView.enableConfirmButton()
        } else if (diproses && dikirim && diterima) {
            mView.showStatus("DITERIMA")
            mView.disableConfirmButton()
        }
    }

    override fun getDataTanggal(tanggal: Date?) {
        if (tanggal != null) {
            mView.showTanggal(tanggal)
        }
    }

    override fun getIdPermintaan(id: String) {
        if (id != "") {
            mView.showIdPermintaan(
                id
            )
        }
    }

    override fun getDataProdukImage(image: String) {
        if (image != "") {
            mView.showProdukImage(image)
        }
    }

    override fun getDataNamaProduk(nama: String) {
        if (nama != "") {
            mView.showNamaProduk(nama)
        }
    }

    override fun getDataJumlahPermintaan(jumlah: Int) {
//        if (jumlah != null){
        mView.showJumlahPermintaan(jumlah.toString())
//        }
    }

    override fun getDataKurir(kurir: String) {
        if (kurir != "") {
            mView.showKurir(kurir)
        } else {
            mView.showKurir("-")
        }
    }

    override fun getDataNoresi(resi: String) {
        if (resi != "") {
            mView.showNoResi(resi)
        }else {
            mView.showNoResi("-")
        }
    }

    override fun getDataNamaPenerima(nama: String) {
        if (nama != "") {
            mView.showNamaPenerima(nama)
        } else {
            mView.showNamaPenerima("-")
        }
    }

    override fun getDataNomorPenerima(no: String) {
        if (no != "") {
            mView.showNoPenerima(no)
        } else {
            mView.showNoPenerima("-")
        }
    }


    override fun getDataAlamat(alamat: String) {
        if (alamat != "") {
            mView.showAlamatPengiriman(alamat)
        }
    }
}