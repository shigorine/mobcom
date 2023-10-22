package com.example.ffff.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ffff.R
import com.example.ffff.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var viewModel: AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = AuthenticationViewModel()
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = viewModel.getUserUid()



        viewModel.getStates().observe(this@PaymentActivity) {
            handleState(it)
        }

        viewModel.getUserProfile()


       binding.btnPaymentConfirm.setOnClickListener {
           viewModel.paymentSenderToReceiver(receiverUid, binding.etEnterAmount.text.toString().toDouble())
           MainActivity.launch(this@PaymentActivity)

       }


    }

    private fun handleState(state : AuthenticationStates) {
        when(state) {
            is AuthenticationStates.Default -> {

            }
            AuthenticationStates.Error -> TODO()
            AuthenticationStates.LogOut -> {
                LoginActivity.launch(this@PaymentActivity)
                finish()
            }
            AuthenticationStates.UserDeleted -> {
                LoginActivity.launch(this@PaymentActivity)
                finish()
            }
            AuthenticationStates.VerificationEmailSent -> TODO()
            else -> {}
        }
    }
}