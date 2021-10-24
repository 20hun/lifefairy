package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityEmailBinding
import com.danny.lifefairy.form.PostModel
import com.danny.lifefairy.form.PostResult
import com.danny.lifefairy.service.SignService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class EmailActivity : AppCompatActivity() {

    private var emailBinding : ActivityEmailBinding? = null
    private val binding get() = emailBinding!!

    lateinit var questionEmail : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        emailBinding = ActivityEmailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var checkNextPage = false

        binding.nextStepAuthEmailBtn.setOnClickListener {
            if (checkNextPage) {
                val intent = Intent(this, PasswordActivity::class.java)
                intent.putExtra("email", questionEmail.text.toString())
                //intent.putExtra("email", "문자열 전달")
                startActivity(intent)
            }
        }

        questionEmail = findViewById(R.id.emailArea)

        val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        fun checkEmail() : Boolean {
            var email = questionEmail.text.toString().trim() //공백제거
            var validEmail = findViewById<TextView>(R.id.validEmail)
            var validBtn = findViewById<ImageView>(R.id.next_step_auth_email_btn)
            val p = Pattern.matches(emailValidation, email) // 서로 패턴이 맞닝?
            if (p) {
                //이메일 형태가 정상일 경우
                validEmail.setText("")
                validBtn.setImageResource(R.drawable.right_click_bg_black_r)
                checkNextPage = true
                //questionEmail.setTextColor(-65536)
                return true
            } else {
                validEmail.setText("이메일 형식이 올바른지 확인해주세요.")
                validBtn.setImageResource(R.drawable.right_click_bg_black)
                checkNextPage = false
                //questionEmail.setTextColor(-65536)
                //또는 questionEmail.setTextColor(R.color.red.toInt())
                return false
            }
        }
        questionEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출
                // s에는 변경 후의 문자열이 담겨 있다.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경되기 전 호출
                // s에는 변경 전 문자열이 담겨 있다.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출된다.
                // 우린 이 함수를 사용한다.
                checkEmail()
            }
        })

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}