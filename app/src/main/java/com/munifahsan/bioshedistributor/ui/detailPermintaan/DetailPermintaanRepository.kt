package com.munifahsan.bioshedistributor.ui.detailPermintaan

import com.munifahsan.bioshedistributor.utils.Constants

class DetailPermintaanRepository(val mListener: DetailPermintaanContract.Listener) :
    DetailPermintaanContract.Repository {

    override fun getDataPermintaan(idPermintaan: String) {
        Constants.REQUEST_PRODUCT_DB.document(idPermintaan).get().addOnSuccessListener {
            if (it.exists()) {
                mListener.getDataStatusListener(
                    it.getBoolean("diproses")!!,
                    it.getBoolean("dikirim")!!,
                    it.getBoolean("selesaiDiterima")!!
                )
                mListener.getDataTanggal(it.getDate("requestDate"))
                mListener.getIdPermintaan(it.id)
                mListener.getDataJumlahPermintaan(it.getLong("jumlahRequest")!!.toInt())
                mListener.getDataKurir(it.getString("kurir").toString())
                mListener.getDataNoresi(it.getString("noResi").toString())
                mListener.getDataAlamat(it.getString("alamatPengiriman").toString())
                getProduk(it.getString("idProdukRequest").toString())
                getDistributor(it.getString("distributorId").toString())
            }
        }
    }

    private fun getProduk(idPermintaan: String) {
        Constants.PRODUCT_DB.document(idPermintaan).get().addOnSuccessListener {
            if (it.exists()) {
                mListener.getDataProdukImage(it.getString("thumbnail").toString())
                mListener.getDataNamaProduk(it.getString("nama").toString())
            }
        }
    }

    private fun getDistributor(idDistributor: String){
        Constants.DISTRIBUTOR_DB.document(idDistributor).get().addOnSuccessListener {
            if (it.exists()){
                mListener.getDataNamaPenerima(it.getString("nama").toString())
                mListener.getDataNomorPenerima(it.getString("noHp").toString())
            }
        }
    }

    override fun updateKonfirmasiDiterima(idPermintaan: String){
        Constants.REQUEST_PRODUCT_DB.document(idPermintaan).update("diterima", true).addOnSuccessListener {
            Constants.REQUEST_PRODUCT_DB.document(idPermintaan).get().addOnSuccessListener {
                if (it.exists()){
                    mListener.getDataStatusListener(
                        it.getBoolean("diproses")!!,
                        it.getBoolean("dikirim")!!,
                        it.getBoolean("selesaiDiterima")!!
                    )
                }
            }
        }
    }
}