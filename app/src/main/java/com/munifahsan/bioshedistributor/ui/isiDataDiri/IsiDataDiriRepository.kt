package com.munifahsan.bioshedistributor.ui.isiDataDiri

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.munifahsan.bioshedistributor.models.Distributor
import com.munifahsan.bioshedistributor.utils.Constants

class IsiDataDiriRepository(private val mListener: IsiDataDiriContract.Listener) :
    IsiDataDiriContract.Repository {


    override fun uploadData(
        imageUri: Uri?, userId: String, email: String, nama: String, nik: String, daerah: String,
        alamat: String, noHp: String
    ) {
        //Upload profile photo to firebase storage
        val fileReference: StorageReference =
            FirebaseStorage.getInstance().reference.child("images/" + userId + "/" + imageUri?.lastPathSegment)
        fileReference
            .putFile(imageUri!!)
            .addOnSuccessListener {

                // download uploaded image url
                fileReference
                    .downloadUrl
                    .addOnSuccessListener {

                        // data model
                        val userData = Distributor(
                            nama,
                            email,
                            it.toString(),
                            alamat,
                            nik,
                            noHp
                        )

                        // post data to firestore
                        Constants.DISTRIBUTOR_DB.document(userId + "1234")
                            .set(userData)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
//                                    binding.horizontalProgressBar.visibility =
//                                        View.INVISIBLE
//                                    showMessage("Register Berhasil")
//                                    startMainActivity()
                                    mListener.savaDataSuccessListener()
                                } else {
                                    mListener.saveDataErrorListener("Error : ${task.exception!!.message}")
//                                    showMessage("Error : ${task.exception!!.message}")
                                }
                            }
                            .addOnFailureListener { exception ->
//                                showMessage("Error : ${exception.message}")
                                mListener.saveDataErrorListener("Error : ${exception.message}")
                            }
                    }
            }
            .addOnProgressListener {
//                binding.horizontalProgressBar.visibility = View.VISIBLE
//                binding.horizontalProgressBar.progress =
//                    (100.0 * it.bytesTransferred / it.totalByteCount).toInt()
//                binding.progressTxt.text = "Mengunggah data..."
            }
    }
}