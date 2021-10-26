package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danny.lifefairy.MainActivity
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityInviteBinding

class InviteActivity : AppCompatActivity() {

    private var inviteBinding : ActivityInviteBinding? = null
    private val binding get() = inviteBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inviteBinding = ActivityInviteBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.noneInviteCode.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}