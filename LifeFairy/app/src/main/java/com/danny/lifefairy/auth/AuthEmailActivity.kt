package com.danny.lifefairy.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityAuthEmailBinding
import com.danny.lifefairy.form.PostModel
import com.danny.lifefairy.form.PostResult
import retrofit2.Callback
import com.danny.lifefairy.service.SignService
import retrofit2.Call
import retrofit2.Response


class AuthEmailActivity : AppCompatActivity() {

    private var emailAuthBinding : ActivityAuthEmailBinding? = null
    private val binding get() = emailAuthBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        emailAuthBinding = ActivityAuthEmailBinding.inflate(layoutInflater)

        setContentView(binding.root)

    }
}