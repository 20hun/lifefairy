package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import com.danny.lifefairy.MainActivity
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityInviteBinding

class InviteActivity : AppCompatActivity() {

    private var inviteBinding : ActivityInviteBinding? = null
    private val binding get() = inviteBinding!!

    private val TAG = "InviteActivity"

    lateinit var inviteCode : TextView

    private var checkNextPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inviteBinding = ActivityInviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noneInviteCode.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        inviteCode = binding.inviteCodeArea

        fun checkInviteCode() {
            var code = inviteCode.text.toString().trim()
            var validBtn = binding.nextStepBtn

            if (code.isNotEmpty()) {
                validBtn.setImageResource(R.drawable.right_click_bg_black_r)
                checkNextPage = true
            } else {
                validBtn.setImageResource(R.drawable.right_click_bg_black)
                checkNextPage = false
            }
        }

        inviteCode.addTextChangedListener(object : TextWatcher {
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
                checkInviteCode()
            }
        })

        binding.nextStepBtn.setOnClickListener {
            if (checkNextPage) {
                // todo
                // 여기서 초대코드 검증, 초대한 사람 맞는지 등 확인
            }
        }
    }
}