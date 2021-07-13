package com.munifahsan.bioshedistributor.models

import com.google.firebase.firestore.DocumentId

class Produk(
    @DocumentId
    val id: String = "",
    val nama: String = "",
    val thumbnail: String = "",
    val keterangan: String = "",
    val promoId: String = "",
    val harga: Int = 0,
    val diskon: Int = 0,
    val stok: Int = 0,
    val berat: Int = 0,
    val sold: Int = 0,
    val selesai: Boolean = false,
    val show: Boolean = false
)
