package com.example.ffff.ui

import android.R
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

import com.example.ffff.databinding.ActivityGenerateQrCodeKotlinBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


class GenerateQrCodeKotlinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenerateQrCodeKotlinBinding
    private lateinit var viewModel : AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityGenerateQrCodeKotlinBinding.inflate(layoutInflater)

        setContentView(binding.root)
        viewModel = AuthenticationViewModel()
        viewModel.getStates().observe(this@GenerateQrCodeKotlinActivity) {
            handleState(it)
        }

        viewModel.getUserProfile()
        val uid = viewModel.getUserUid()

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(uid, BarcodeFormat.QR_CODE, 400, 400)
            val imageViewQrCode = findViewById<View>(com.example.ffff.R.id.qrCodeImageView) as ImageView
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
        }






    }



    private fun handleState(state : AuthenticationStates) {
        when(state) {
            is AuthenticationStates.Default -> {

            }
            AuthenticationStates.Error -> TODO()
            AuthenticationStates.LogOut -> {
                LoginActivity.launch(this@GenerateQrCodeKotlinActivity)
                finish()
            }
            AuthenticationStates.UserDeleted -> {
                LoginActivity.launch(this@GenerateQrCodeKotlinActivity)
                finish()
            }
            AuthenticationStates.VerificationEmailSent -> TODO()
            else -> {}
        }
    }
}