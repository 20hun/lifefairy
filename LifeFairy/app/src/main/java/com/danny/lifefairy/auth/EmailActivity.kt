package com.danny.lifefairy.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danny.lifefairy.R
import kotlinx.android.synthetic.main.activity_email.*

class EmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}