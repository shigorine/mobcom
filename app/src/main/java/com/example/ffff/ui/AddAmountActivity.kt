package com.example.ffff.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ffff.R
import com.example.ffff.databinding.ActivityAddAmountBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityAddAmountBinding
private lateinit var viewModel: AuthenticationViewModel
private var auth = Firebase.auth
private val ref = Firebase.database.reference

class AddAmountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAmountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthenticationViewModel()

        val currentUser = auth.currentUser


        with(binding) {
            btnSaveAmount.setOnClickListener {
                val newBalance = binding.tieEnterAmount.text.toString()
                if (newBalance.isNotEmpty() && currentUser != null) {
                    val currentUserEmail = currentUser.email
                    if (currentUserEmail != null) {
                        val userMoneyRef = ref.child("bankapp/users/$currentUserEmail/money")
                        userMoneyRef.setValue(newBalance).addOnSuccessListener {
                            viewModel.updateUserBalance(newBalance)
                        }.addOnFailureListener {
                        }
                    }
                }


            }
        }
    }
}