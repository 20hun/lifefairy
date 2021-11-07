package com.danny.lifefairy

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.icu.util.UniversalTimeScale.toLong
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.danny.lifefairy.auth.GlobalApplication
import com.danny.lifefairy.auth.IntroActivity
import com.danny.lifefairy.databinding.ActivityMainBinding
import com.danny.lifefairy.service.SignService
import com.google.android.datatransport.runtime.util.PriorityMapping.toInt
import com.google.android.gms.common.server.converter.StringToIntConverter
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var mainBinding : ActivityMainBinding? = null
    private val binding get() = mainBinding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val now = System.currentTimeMillis()
        val now2 = LocalDate.now()
        var day_list = mutableListOf<String>()
        for (i in -6..6) {
            day_list.add(now2.plusDays(i.toLong()).toString())
        }
        // Log.d("yoil", day_list.toString())
        Log.d("yoil", now2.toString()) //2021-11-06


        val yearMonthForm = SimpleDateFormat("yyyy년MM월", Locale.KOREA).format(now)
        binding.calendarYearMonthText.setText(yearMonthForm)
        val dayName = SimpleDateFormat("E", Locale.KOREA).format(now)
        //val day = SimpleDateFormat("dd", Locale.KOREA).format(now)
        Log.d("yoil", dayName)
        val dayName_list = listOf(binding.mon, binding.tue, binding.wed, binding.thu, binding.fri, binding.sat, binding.sun)
        when (dayName) {
            "월" -> {
                for (i in 6..12){
                    val day = day_list[i].split('-')[2]
                    dayName_list[i-6].setText(day)
                }
            }
            "일" -> {
                for (i in 0..6){
                    val day = day_list[i].split('-')[2]
                    dayName_list[i].setText(day)
                }
            }
            else -> binding.mon.setText("21")
        }

        thread {
            val api = SignService.tokenRequest(GlobalApplication.prefs.getString("accessToken", ""))
            val resp = api.get_nudge_text().execute()

            if (resp.isSuccessful) {
                Log.d("nudgeText", resp.body()?.data?.mainMsg.toString())
                Log.d("nudgeText", resp.body()?.data?.feedback.toString())

                // val uni = "\ud83d\udc4d" -> "U+1F44D" java escape 형태
                // Log.d("nudgeText", 44032.toChar().toString()) "가" 출력됨 44032가 유티코드의 16진수 부분을 변환한 10진수

                val strEmoji = resp.body()?.data?.feedback?.emoji.toString() // "U+1F44D"
                val strEmoji2 = strEmoji.replace("U+", "0x")
                val intEmoji = Integer.decode(strEmoji2) // string to int 해야 함 val x = 0x1F44D
                binding.nudgeText.setText(resp.body()?.data?.mainMsg.toString().plus(String(Character.toChars(intEmoji))))
            } else {
                Toast.makeText(this, "nudge 가져오기 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

//        val keyHash = Utility.getKeyHash(this)
//        Log.d("KEY_HASH", keyHash)
//        val kakao_logout_button = findViewById<Button>(R.id.kakao_logout_button) // 로그인 버튼
//
//        kakao_logout_button.setOnClickListener {
//            UserApiClient.instance.logout { error ->
//                if (error != null) {
//                    Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
//                }else {
//                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
//                }
//                val intent = Intent(this, IntroActivity::class.java)
//                startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
//                finish()
//            }
//        }
//
//        val kakao_unlink_button = findViewById<Button>(R.id.kakao_unlink_button) // 로그인 버튼
//
//        kakao_unlink_button.setOnClickListener {
//            UserApiClient.instance.unlink { error ->
//                if (error != null) {
//                    Toast.makeText(this, "회원 탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
//                }else {
//                    Toast.makeText(this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
//                    finish()
//                }
//            }
//        }
    }
}