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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : AppCompatActivity() {

    private var splashBinding : ActivitySplashBinding? = null
    private val binding get() = splashBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashBinding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.crapRising.animate().translationY(-600F).setDuration(3000).setStartDelay(0)
        binding.crapRising2.animate().translationY(-700F).setDuration(4000).setStartDelay(0)
        binding.crapRising3.animate().translationY(-800F).setDuration(5000).setStartDelay(0)

        // 로그인 한 상태면 -> 메인 화면
        // 로그인해야 하면 -> intro 회원가입 화면
        // 토큰이 기기에 저장되어 있음

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("devicetoken", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            val msg = getString(R.string.msg_token_fmt, token)
            Log.e("devicetoken", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            Log.e("devicetoken", GlobalApplication.prefs.getString("accessToken", "비어있음"))

            GlobalApplication.prefs.setString("deviceToken", msg)
        })

        Handler().postDelayed({
            val intent = Intent(this, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, 3000)
    }
}