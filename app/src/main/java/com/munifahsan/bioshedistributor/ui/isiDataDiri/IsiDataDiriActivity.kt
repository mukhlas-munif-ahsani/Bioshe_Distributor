package com.munifahsan.bioshedistributor.ui.isiDataDiri

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.munifahsan.bioshedistributor.MainActivity
import com.munifahsan.bioshedistributor.databinding.ActivityIsiDataDiriBinding
import com.munifahsan.bioshedistributor.ui.splashScreen.SplashScreenActivity
import com.munifahsan.bioshedistributor.utils.FileUtil
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern

class IsiDataDiriActivity : AppCompatActivity(), IsiDataDiriContract.View {
    private lateinit var binding: ActivityIsiDataDiriBinding
    private lateinit var mPres: IsiDataDiriContract.Presenter
    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1
    private var actualImage: File? = null
    private var compressedImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIsiDataDiriBinding.inflate(layoutInflater)
        val view = binding.root

        mPres = IsiDataDiriPresenter(this)

        auth = FirebaseAuth.getInstance()

        binding.cardImageProfile.setOnClickListener {
            openFileChooser()
        }

        binding.saveBtn.setOnClickListener {
            getInput()
        }

        setContentView(view)
    }

    private fun getInput() {
        val nama = binding.namaEdt.text.toString()
        val nik = binding.nikEdt.text.toString()
        val daerah = binding.daerahEdt.text.toString()
        val alamat = binding.alamatEdt.text.toString()
        val noHp = binding.nohpEdt.text.toString()

        if (mPres.isFormValid(compressedImage, nama, nik, daerah, alamat, noHp)) {
            compressedImage?.let {
                mPres.saveData(
                    it.absoluteFile.toUri(),
                    auth.currentUser!!.uid,
                    auth.currentUser!!.email.toString(),
                    nama,
                    nik,
                    daerah,
                    alamat,
                    noHp
                )
                binding.saveBtn.isEnabled = false
                binding.saveProgress.visibility = View.VISIBLE
//                showMessage(auth.currentUser!!.email.toString())
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        resultLauncher.launch(intent)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(PICK_IMAGE_REQUEST, result, result.data)

        }

    private fun onActivityResult(requestCode: Int, result: ActivityResult, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && result.resultCode == RESULT_OK && data != null && data.data != null) {
            actualImage = FileUtil.from(this, data.data)
            customCompressImage()
        }
    }

    private fun customCompressImage() {
        actualImage?.let { imageFile ->
            lifecycleScope.launch {
                // Default compression with custom destination file
                /*compressedImage = Compressor.compress(this@MainActivity, imageFile) {
                    default()
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                        val file = File("${it.absolutePath}${File.separator}my_image.${imageFile.extension}")
                        destination(file)
                    }
                }*/

                // Full custom
                compressedImage = Compressor.compress(this@IsiDataDiriActivity, imageFile) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(1_097_152) // 1 MB
                }
                setCompressedImage()
            }
        } ?: showMessage("Please choose an image!")
    }

    override fun showMessage(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    private fun setCompressedImage() {
        compressedImage?.let {
            binding.imgProfileImage.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
//            binding.textSize.text = String.format("Size : %s", getReadableFileSize(it.length()))
//            binding.upload.isEnabled = true
            //showMessage("Compressed image save in ${it.absoluteFile.toURI()}")
            //Toast.makeText(this, "Compressed image save in ${it.absoluteFile.toURI()}", Toast.LENGTH_LONG).show()
            Log.d("Compressor", "Compressed image save in " + it.path)
        }
    }

    override fun showImageError(error: String) {
        binding.progileImageError.visibility = View.VISIBLE
        binding.progileImageError.text = error
    }

    override fun hideImageError() {
        binding.progileImageError.visibility = View.GONE
    }

    override fun showNamaError(error: String) {
        binding.namaInLay.error = error
    }

    override fun hideNamaError() {
        binding.namaInLay.isErrorEnabled = false
    }

    override fun showNikError(error: String) {
        binding.nikInLay.error = error
    }

    override fun hideNikError() {
        binding.nikInLay.isErrorEnabled = false
    }

    override fun showDaerahError(error: String) {
        binding.daerahInLay.error = error
    }

    override fun hideDaerahError() {
        binding.daerahInLay.isErrorEnabled = false
    }

    override fun showAlamatError(error: String) {
        binding.alamatInLay.error = error
    }

    override fun hideAlamatError() {
        binding.alamatInLay.isErrorEnabled = false
    }

    override fun showNohpError(error: String) {
        binding.nohpInLay.error = error
    }

    override fun hideNohpError() {
        binding.nohpInLay.isErrorEnabled = false
    }

    override fun saveDataError(){
        binding.saveBtn.isEnabled = true
        binding.saveProgress.visibility = View.INVISIBLE
    }

    override fun startMainActivity(){
        val intent = Intent(this, SplashScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}