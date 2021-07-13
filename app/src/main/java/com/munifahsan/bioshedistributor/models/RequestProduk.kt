package com.munifahsan.bioshedistributor.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.sql.Timestamp
import java.util.*

data class RequestProduk(
    @DocumentId
    var id: String = "",
    var idProdukRequest: String = "",
    var jumlahRequest: Int = 0,
    var jumlahDikirim: Int = 0,
    @ServerTimestamp
    var requestDate: Date? = null,
    var distributorId: String = "",
    var alamatPengiriman: String = "",
    var diproses: Boolean = false,
//    @ServerTimestamp
    var diprosesDate: Date? = null,
    var dikirim: Boolean = false,
//    @ServerTimestamp
    var dikirimDate: Date? = null,
    var selesaiDiterima: Boolean = false,
//    @ServerTimestamp
    var selesaiDiterimaDate: Date? = null,
    var show: Boolean = false,
    var kurir: String = "",
    var noResi: String = "",
    var catatan: String = "",
    var updated: Timestamp? = null
)
