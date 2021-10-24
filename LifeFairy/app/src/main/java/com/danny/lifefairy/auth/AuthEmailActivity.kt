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

    val api = SignService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        emailAuthBinding = ActivityAuthEmailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val emoji = intent.getStringExtra("emoji")
        Toast.makeText(this, emoji, Toast.LENGTH_LONG).show()

        binding.testBtn.setOnClickListener {

            val email = findViewById<EditText>(R.id.one_btn)
            val password = findViewById<EditText>(R.id.two_btn)
            val name = findViewById<EditText>(R.id.three_btn)
            val emoji = findViewById<EditText>(R.id.four_btn)

            val data = PostModel(
                email.text.toString(),
                password.text.toString(),
                name.text.toString(),
                emoji.text.toString()
            )

            api.post_users(data).enqueue(object : Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    Log.d("log222", response.toString())
                    Log.d("log222", response.body().toString())
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    // 실패
                    Log.d("log222", t.message.toString())
                    Log.d("log222", "fail")
                }
            })
        }
    }
}