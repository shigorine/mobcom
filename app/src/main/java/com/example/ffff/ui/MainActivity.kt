package com.example.ffff.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ffff.databinding.ActivityMainBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthenticationViewModel()
        viewModel.getStates().observe(this@MainActivity) {
            handleState(it)
        }

        viewModel.getUserProfile()

        with(binding){
            btnLogOut.setOnClickListener {
                viewModel.logOut()
            }



            btnGenerateQrCode.setOnClickListener {
                val intent = Intent(this@MainActivity, GenerateQrCodeKotlinActivity::class.java)
                startActivity(intent)
            }

            btnTransferFunds.setOnClickListener {
//                val intent = Intent(this@MainActivity, ScanActivity::class.java)
//                startActivity(intent)

                barcodeLauncher.launch(ScanOptions())
            }

        }


     }

    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            val intent = Intent(this@MainActivity, PaymentActivity::class.java)
            intent.putExtra("uid",result.contents)
            startActivity(intent)

            Toast.makeText(
                this@MainActivity,
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()
        }
    }



    private fun handleState(state : AuthenticationStates) {
        when(state) {
            is AuthenticationStates.Default -> {
                binding.tvUser.text = "Welcome, ${state.user?.name}!"
                binding.tvUserMoney.text = "You have ${state.user?.money}"
            }
            AuthenticationStates.Error -> TODO()
            AuthenticationStates.LogOut -> {
                LoginActivity.launch(this@MainActivity)
                finish()
            }
            AuthenticationStates.UserDeleted -> {
                LoginActivity.launch(this@MainActivity)
                finish()
            }
            AuthenticationStates.VerificationEmailSent -> TODO()
            else -> {}
        }
    }
    companion object {
        fun launch(activity : Activity) = activity.startActivity(Intent(activity, MainActivity::class.java))
    }
    }






