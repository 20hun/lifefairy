package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityNicknameBinding

class NicknameActivity : AppCompatActivity() {

    private var nicknameBinding : ActivityNicknameBinding? = null
    private val binding get() = nicknameBinding!!

    lateinit var questionNickname : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nicknameBinding = ActivityNicknameBinding.inflate(layoutInflater)

        setContentView(binding.root)


        var checkNextPage = false
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
//        Toast.makeText(this, value1, Toast.LENGTH_LONG).show()
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        questionNickname = findViewById(R.id.nicknameArea)

        fun checkNickname() : Boolean {
            var nickname = questionNickname.text.toString().trim() //공백제거
            var validNickname = findViewById<TextView>(R.id.validNickname)
            var validBtn = findViewById<ImageView>(R.id.next_step_auth_emoji_btn)
            if (nickname.length in 1..3) {
                //이메일 형태가 정상일 경우
                validNickname.setText("")
                validBtn.setImageResource(R.drawable.right_click_bg_black_r)
                checkNextPage = true
                //questionEmail.setTextColor(-65536)
                return true
            } else {
                validNickname.setText("비밀번호 조건을 확인해주세요.")
                validBtn.setImageResource(R.drawable.right_click_bg_black)
                checkNextPage = false
                //questionEmail.setTextColor(-65536)
                //또는 questionEmail.setTextColor(R.color.red.toInt())
                return false
            }
        }
        questionNickname.addTextChangedListener(object : TextWatcher {
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
                checkNickname()
            }
        })


        binding.nextStepAuthEmojiBtn.setOnClickListener {
            if (checkNextPage) {
                val intent = Intent(this, EmojiActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                intent.putExtra("name", questionNickname.text.toString())
                startActivity(intent)
            }
        }
    }
}