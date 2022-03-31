package com.danny.lifefairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.danny.lifefairy.auth.GlobalApplication
import com.danny.lifefairy.auth.IntroActivity
import com.danny.lifefairy.databinding.ActivitySplashBinding
import com.danny.lifefairy.form.PostDeviceTokenModel
import com.danny.lifefairy.form.PostLoginModel
import com.danny.lifefairy.service.SignService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {

    private var splashBinding : ActivitySplashBinding? = null
    private val binding get() = splashBinding!!

    private val TAG = "SplashActivity"

    private var accessTokenCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashBinding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.crapRising.animate().translationY(-600F).setDuration(3000).setStartDelay(0)
        binding.crapRising2.animate().translationY(-700F).setDuration(4000).setStartDelay(0)
        binding.crapRising3.animate().translationY(-800F).setDuration(5000).setStartDelay(0)

        // 로그인 한 상태면 -> 메인 화면
        // 로그인해야 하면 -> intro 회원가입 화면
        // refresh token SharedPreferences 저장
        // access token 메모리에 올려 놓고 활용

        // GlobalApplication.prefs.setString("refreshToken", "eyJhbibmFtZSI6Imx5aCIsImlhdCI6MTYzOTIwMTI5NSwiZXhwIjoxNjQxNzkzMjk1LCJpc3MiOiJsaWZlZmFpcnkiLCJqdGkiOiJhYmM4MmJjOThjMmVmZDNiNGE2OWFkOGZmOTJmZTkxMGZhMjY3MjM5MDVjNDQxYzQ3MzNkYTYyMGNkYTYyMGJjY2Y5NGY4MGM3MzJhOWM1NCJ9.-j_4U7wdcrzy8wNMqh4CY-krqqbO-ZdldtShIEgMo58")

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // val msg = getString(R.string.msg_token_fmt, token)  // msg = 토큰 : ~~~
            Log.e(TAG, token)
            GlobalApplication.prefs.setString("deviceToken", token) // device Token 로그인할 때 등록하자

            // access token 있으면 바로 등록
            // refresh token 기기에 있는지 + 유효한지 확인
            if (GlobalApplication.prefs.getString("refreshToken", "비어있음") == "비어있음"){
                // 로그인 화면으로
                Log.e(TAG, "refresh token 없음")
            } else {

                thread {
                    val api = SignService.tokenRequest(GlobalApplication.prefs.getString("refreshToken", ""))
                    val resp = api.from_refresh_get_access().execute()

                    if (resp.isSuccessful) {
                        Log.e(TAG, resp.body()?.access_token.toString())

                        val message = resp.body()?.message.toString()
                        if (message.contains("새로운")){
                            GlobalApplication.prefs.setString("accessToken", resp.body()?.access_token.toString())
                            accessTokenCheck = true
                        }
                    } else {
                        Log.e(TAG, "refresh 로 access token 가져오기 통신 실패 : 올바르지 않거나 만료된 refresh token")
                    }
                }
            }
        })

        Log.e(TAG, GlobalApplication.prefs.getString("refreshToken", "비어있음"))

        Handler().postDelayed({

            var intent = Intent(this, IntroActivity::class.java)
            if (accessTokenCheck) { // access token 존재
                intent = Intent(this, MainActivity::class.java)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, 3000)
    }
}