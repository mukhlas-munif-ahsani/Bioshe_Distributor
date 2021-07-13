package com.munifahsan.bioshedistributor.models

import com.google.firebase.firestore.DocumentId

data class Distributor(
    val nama: String = "",
    val email: String = "",
    val photo_url: String = "",
    val daerah: String = "",
    val alamat: String = "",
    val nik: String = "",
    val noHp: String = ""
)
