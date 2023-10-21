package com.example.ffff.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ffff.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var viewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthenticationViewModel()
        viewModel.getStates().observe(this@SignUpActivity) {
            handleState(it)
        }

        binding.btnSignup.setOnClickListener {
            viewModel.signUp(
                binding.tieEmail.text.toString(),
                binding.tiePassword.text.toString()
            )
        }

    }

    private fun handleState(state : AuthenticationStates) {
        when(state) {
            is AuthenticationStates.SignedUp -> viewModel.createUserRecord(
                binding.tieEmail.text.toString(),
                binding.tieName.text.toString(), "0")


            is AuthenticationStates.ProfileUpdated -> {
                LoginActivity.launch(this@SignUpActivity)
                finish()
            }
            else -> {}
        }
    }


    companion object {
        fun launch(activity: Activity) = activity.startActivity(Intent(activity, SignUpActivity::class.java))
    }
}