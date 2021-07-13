package com.munifahsan.bioshedistributor.ui.isiDataDiri

import android.net.Uri
import java.io.File

interface IsiDataDiriContract {
    interface View{
        fun showNamaError(error: String)
        fun hideNamaError()
        fun showNikError(error: String)
        fun hideNikError()
        fun showAlamatError(error: String)
        fun hideAlamatError()
        fun showNohpError(error: String)
        fun hideNohpError()
        fun showImageError(error: String)
        fun hideImageError()
        fun startMainActivity()
        fun showMessage(s: String)
        fun saveDataError()
        fun hideDaerahError()
        fun showDaerahError(error: String)
    }
    interface Presenter{
        fun isFormValid(
            imageUri: File?,
            nama: String,
            nik: String,
            daerah: String,
            alamat: String,
            noHp: String
        ): Boolean

        fun saveData(
            imageUri: Uri?,
            userId: String,
            email: String,
            nama: String,
            nik: String,
            daerah: String,
            alamat: String,
            noHp: String
        )
    }
    interface Repository{
        fun uploadData(
            imageUri: Uri?,
            userId: String,
            email: String,
            nama: String,
            nik: String,
            daerah: String,
            alamat: String,
            noHp: String
        )
    }
    interface Listener{
        fun savaDataSuccessListener()
        fun saveDataErrorListener(error: String)
    }
}