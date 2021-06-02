package com.munifahsan.biosheadmin.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Constants {
    companion object {
        /*
         FIRESTORE
         */
        val CURRENT_USER = FirebaseAuth.getInstance().currentUser
        val CURRENT_USER_ID = FirebaseAuth.getInstance().currentUser?.uid
        val AUTH = FirebaseAuth.getInstance()


        val USERS_DB = FirebaseFirestore.getInstance()
            .collection("USERS")
        val SALES_DB = FirebaseFirestore.getInstance()
            .collection("SALES")
        val DISTRIBUTOR_DB = FirebaseFirestore.getInstance()
            .collection("DISTRIBUTOR")
        val PRODUCT_DB = FirebaseFirestore.getInstance()
            .collection("PRODUCT")
        val PROMO_DB = FirebaseFirestore.getInstance()
            .collection("PROMO")
        val REWARD_DB = FirebaseFirestore.getInstance()
            .collection("REWARD")
        val ADMIN_DB = FirebaseFirestore.getInstance()
            .collection("ADMIN")
        val PRODUK_DRAFT_DB = FirebaseFirestore.getInstance()
            .collection("DRAFT").document("PRODUK")
        val PROMO_DRAFT_DB = FirebaseFirestore.getInstance()
            .collection("DRAFT").document("PROMO")
        val ORDERS_DB = FirebaseFirestore.getInstance()
            .collection("ORDERS")

    }
}