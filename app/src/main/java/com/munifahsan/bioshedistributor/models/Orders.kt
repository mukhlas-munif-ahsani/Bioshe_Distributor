package com.munifahsan.bioshedistributor.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Orders(
    @DocumentId
    var id: String = "",
    @ServerTimestamp
    var orderDate: Date? = null,
    var orderStatus:String = "",
    var kurir: String = "",
    var resi: String = "",
    var alamatPengiriman: String = "",
    var userId: String = "",
    var salesId: String = "",
    var midtransOrderId: String = "",
    var metodePembayaran: String = "",
    var ongkir: Int = 0,
    var membayar: Boolean = false,
    var dibayar: Boolean = false,
    var diproses: Boolean = false,
    var dikirim: Boolean = false,
    var selesaiDikirim: Boolean = false,
    var selesaiDiterima: Boolean = false,
    var show: Boolean = false
)
