package com.danny.lifefairy.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.danny.lifefairy.auth.GlobalApplication
import com.danny.lifefairy.auth.IntroActivity
import com.danny.lifefairy.databinding.ActivityMyPageBinding
import com.danny.lifefairy.service.SignService
import kotlin.concurrent.thread

class MyPageActivity : AppCompatActivity() {

    private var myPageBinding : ActivityMyPageBinding?= null
    private val binding get() = myPageBinding!!

    private val TAG = "MyPageActivity"

    private val api = SignService.tokenRequest(GlobalApplication.prefs.getString("accessToken", ""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPageBinding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutBtn.setOnClickListener {
            // 로그아웃
            // intro 화면으로
            // refresh token 삭제
            GlobalApplication.prefs.setString("refreshToken", "")
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

        binding.inviteCodeCreate.setOnClickListener {
            thread {
                val resp = api.invite_code_create().execute()
                if (resp.isSuccessful){
                    runOnUiThread {
                        binding.inviteCode.setText(resp.body()?.invitationCode.toString())
                    }
                }
            }
        }

    }
}