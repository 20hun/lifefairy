package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityLoginBinding
import com.danny.lifefairy.form.PostEmailCheckModel
import com.danny.lifefairy.form.PostLoginModel
import com.danny.lifefairy.service.SignService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {

    private var loginBinding : ActivityLoginBinding? = null
    private val binding get() = loginBinding!!

    lateinit var questionPassword : TextView
    var checkNextPage = false

    val api = SignService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email")
        binding.emailArea.setText(email)
        binding.passwordArea.requestFocus()

        questionPassword = findViewById(R.id.passwordArea)

        fun checkPassword() {
            var password = questionPassword.text.toString().trim() //공백제거
            var validBtn = findViewById<ImageView>(R.id.next_step_auth_password_btn)
            if (password.isNotEmpty()) {
                //이메일 형태가 정상일 경우
                validBtn.setImageResource(R.drawable.right_click_bg_black_r)
                checkNextPage = true
            } else {
                validBtn.setImageResource(R.drawable.right_click_bg_black)
                checkNextPage = false
            }
        }

        questionPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출된다.
                // 우린 이 함수를 사용한다.
                checkPassword()
            }
        })

        binding.nextStepAuthPasswordBtn.setOnClickListener {
            if (checkNextPage) {
                val data = PostLoginModel(
                    email,
                    questionPassword.text.toString().trim()
                )

                thread {
                    val resp = api.post_login(data).execute()
                    CoroutineScope(Dispatchers.Main).launch {
                        if (resp.isSuccessful) {
                            Log.d("log223", resp.toString())
                            Log.d("log223", resp.body().toString())
                            val api2 = SignService.tokenRequest(resp.body()?.access_token.toString())
                            GlobalApplication.prefs.setString("accessToken", resp.body()?.access_token.toString())
                            thread {
                                val tf = api2.get_space_check().execute()
                                Log.d("log223", tf.body().toString())
                            }
                            val intent = Intent(this@LoginActivity, InviteActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.d("log223", resp.toString())
                            Log.d("log223", "로그인 실패")
                        }
                    }
                }
            }
        }
    }
}