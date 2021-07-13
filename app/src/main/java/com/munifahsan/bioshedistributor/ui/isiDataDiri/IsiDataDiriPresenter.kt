package com.munifahsan.bioshedistributor.ui.isiDataDiri

import android.net.Uri
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern

class IsiDataDiriPresenter(private val mView: IsiDataDiriContract.View) :
    IsiDataDiriContract.Presenter, IsiDataDiriContract.Listener {
    private val mRepo: IsiDataDiriContract.Repository

    init {
        mRepo = IsiDataDiriRepository(this)
    }

    override fun saveData(
        imageUri: Uri?, userId: String, email: String, nama: String, nik: String, daerah: String,
        alamat: String, noHp: String
    ) {
        mRepo.uploadData(imageUri, userId, email, nama, nik, daerah, alamat, noHp)
    }

    override fun isFormValid(
        imageUri: File?, nama: String, nik: String, daerah: String,
        alamat: String, noHp: String
    ): Boolean {
        var isValid = true

        if (nama.isEmpty()) {
            isValid = false
            mView.showNamaError("Nama tidak boleh kosong")
        }

        if (nama.isNotEmpty()) {
            mView.hideNamaError()
        }

        if (imageUri == null) {
            isValid = false
            mView.showImageError("Foto harus dipilih !!")
        } else {
            mView.hideImageError()
        }

        if (nik.isEmpty()) {
            isValid = false
            mView.showNikError("NIK tidak boleh kosong")
        }

        if (nik.isNotEmpty()) {
            mView.hideNikError()
        }

//        if (gender.isEmpty()) {
//            isValid = false
//            binding.genderInLay.error = "Jenis kelamin tidak boleh kosong"
//        }
//
//        if (gender.isNotEmpty()) {
//            binding.genderInLay.isErrorEnabled = false
//        }
        if (daerah.isEmpty()) {
            isValid = false
            mView.showDaerahError("Daerah tidak boleh kosong")
        }

        if (daerah.isNotEmpty()) {
            mView.hideDaerahError()
        }

        if (alamat.isEmpty()) {
            isValid = false
            mView.showAlamatError("Alamat tidak boleh kosong")
        }

        if (alamat.isNotEmpty()) {
            mView.hideAlamatError()
        }

        if (noHp.isEmpty()) {
            isValid = false
            mView.showNohpError("No Hp tidak boleh kosong")
        }

        if (noHp.isNotEmpty()) {
            mView.hideNohpError()
        }

        return isValid
    }

    override fun savaDataSuccessListener(){
        mView.startMainActivity()
    }

    override fun saveDataErrorListener(error: String){
        mView.showMessage(error)
        mView.saveDataError()
    }

    private fun isEmailValid(email: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }
}