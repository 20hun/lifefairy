package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityAuthEmailBinding
import com.danny.lifefairy.form.PostEmailAuthModel
import com.danny.lifefairy.form.PostModel
import retrofit2.Callback
import com.danny.lifefairy.service.SignService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import kotlin.concurrent.thread


class AuthEmailActivity : AppCompatActivity() {

    private var emailAuthBinding : ActivityAuthEmailBinding? = null
    private val binding get() = emailAuthBinding!!

    var checkNextPage = false

    val api = SignService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        emailAuthBinding = ActivityAuthEmailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val email = intent.getStringExtra("email")

        binding.authNumArea1.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출
                binding.authNumArea1.setBackgroundResource(R.drawable.border_black)
                binding.authNumArea2.requestFocus()
            }

        })
        binding.authNumArea2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출
                binding.authNumArea2.setBackgroundResource(R.drawable.border_black)
                binding.authNumArea3.requestFocus()
            }

        })
        binding.authNumArea3.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출
                binding.authNumArea3.setBackgroundResource(R.drawable.border_black)
                binding.authNumArea4.requestFocus()
            }

        })
        binding.authNumArea4.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출
                binding.authNumArea4.setBackgroundResource(R.drawable.border_black)
                binding.authNumArea5.requestFocus()
            }

        })
        binding.authNumArea5.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출
                binding.authNumArea5.setBackgroundResource(R.drawable.border_black)
                binding.authNumArea6.requestFocus()
            }

        })
        binding.authNumArea6.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출
                binding.authNumArea6.setBackgroundResource(R.drawable.border_black)
                checkNextPage = true
                binding.nextStepAuthEmailBtn.setImageResource(R.drawable.right_click_bg_black_r)
            }

        })

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.nextStepAuthEmailBtn.setOnClickListener {
            if (checkNextPage) {
                // 통신
                val confirmCode = binding.authNumArea1.text.toString() + binding.authNumArea2.text.toString() + binding.authNumArea3.text.toString() + binding.authNumArea4.text.toString() + binding.authNumArea5.text.toString() + binding.authNumArea6.text.toString()
                val data = PostEmailAuthModel(
                    email,
                    confirmCode
                )

                thread {
                    val resp = api.post_email_auth(data).execute()
                    CoroutineScope(Dispatchers.Main).launch {
                        if (resp.isSuccessful) {
                            val intent = Intent(this@AuthEmailActivity, LoginActivity::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                        } else {
                            Log.d("log222", "인증번호 틀림")
                        }
                    }
                }
            }
        }

    }
}