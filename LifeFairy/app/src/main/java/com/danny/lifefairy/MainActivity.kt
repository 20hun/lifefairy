package com.danny.lifefairy

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.icu.util.UniversalTimeScale.toLong
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.danny.lifefairy.auth.GlobalApplication
import com.danny.lifefairy.auth.IntroActivity
import com.danny.lifefairy.databinding.ActivityMainBinding
import com.danny.lifefairy.form.PostDeviceTokenModel
import com.danny.lifefairy.form.SpaceId
import com.danny.lifefairy.mainrv.RVMainAdapter
import com.danny.lifefairy.mypage.MyPageActivity
import com.danny.lifefairy.service.SignService
import com.google.android.datatransport.runtime.util.PriorityMapping.toInt
import com.google.android.gms.common.server.converter.StringToIntConverter
import com.google.gson.JsonObject
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var mainBinding : ActivityMainBinding? = null
    private val binding get() = mainBinding!!

    private val TAG = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e(TAG, "access Token: " + GlobalApplication.prefs.getString("accessToken", ""))

        binding.myPageBtn.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        // device 토큰 바로 등록
        thread {
            val data = PostDeviceTokenModel(
                GlobalApplication.prefs.getString("deviceToken", "")
            )

            val api = SignService.tokenRequest(GlobalApplication.prefs.getString("accessToken", ""))
            val resp = api.device_token_post(data).execute()

            val message = resp.body()?.message.toString()
            if (message.contains("등록했습니다")){
                Log.e(TAG, "device 토큰 등록 성공")
            } else {
                Log.e(TAG, "device 토큰 등록 실패")
            }
        }

        val now = System.currentTimeMillis()
        val now2 = LocalDate.now()
        var day_list = mutableListOf<String>()
        for (i in -6..6) {
            day_list.add(now2.plusDays(i.toLong()).toString())
        }
        // Log.d("yoil", day_list.toString())
        Log.d("yoil", now2.toString()) //2021-11-06


        val yearMonthForm = SimpleDateFormat("yyyy년MM월", Locale.KOREA).format(now)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(now)
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
                binding.monL.setBackgroundResource(R.drawable.border_none)
            }
            "화" -> {
                for (i in 5..11){
                    val day = day_list[i].split('-')[2]
                    dayName_list[i-5].setText(day)
                }
                binding.tueL.setBackgroundResource(R.drawable.border_none)
            }
            "수" -> {
                for (i in 4..10){
                    val day = day_list[i].split('-')[2]
                    dayName_list[i-4].setText(day)
                }
                binding.wedL.setBackgroundResource(R.drawable.border_none)
            }
            "목" -> {
                for (i in 3..9){
                    val day = day_list[i].split('-')[2]
                    dayName_list[i-3].setText(day)
                }
                binding.thuL.setBackgroundResource(R.drawable.border_none)
            }
            "금" -> {
                for (i in 2..8){
                    val day = day_list[i].split('-')[2]
                    dayName_list[i-2].setText(day)
                }
                binding.friL.setBackgroundResource(R.drawable.border_none)
            }
            "토" -> {
                for (i in 1..7){
                    val day = day_list[i].split('-')[2]
                    dayName_list[i-1].setText(day)
                }
                binding.satL.setBackgroundResource(R.drawable.border_none)
            }
            "일" -> {
                for (i in 0..6){
                    val day = day_list[i].split('-')[2]
                    dayName_list[i].setText(day)
                }
                binding.sunL.setBackgroundResource(R.drawable.border_none)
            }
            else -> binding.mon.setText("else")
        }

        thread {
            val api = SignService.tokenRequest(GlobalApplication.prefs.getString("accessToken", ""))
            val resp = api.get_nudge_text().execute()
            Log.d("nudgeText", GlobalApplication.prefs.getString("accessToken", ""))

            if (resp.isSuccessful) {
                Log.d("nudgeText", resp.body()?.data?.mainMsg.toString())
                Log.d("nudgeText", resp.body()?.data?.feedback.toString())

                // val uni = "\ud83d\udc4d" -> "U+1F44D" java escape 형태
                // Log.d("nudgeText", 44032.toChar().toString()) "가" 출력됨 44032가 유티코드의 16진수 부분을 변환한 10진수

                val strEmoji = resp.body()?.data?.feedback?.emoji.toString() // "U+1F44D"
                val strEmoji2 = strEmoji.replace("U+", "0x")
                val intEmoji = Integer.decode(strEmoji2) // string to int 해야 함 val x = 0x1F44D
                runOnUiThread {
                    binding.nudgeText.setText(resp.body()?.data?.mainMsg.toString().plus(String(Character.toChars(intEmoji))))
                }
            } else {
                Toast.makeText(this, "nudge 가져오기 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        thread {
            val api = SignService.tokenRequest(GlobalApplication.prefs.getString("accessToken", ""))
            val resp2 = api.get_space_user(today).execute()
            Log.d("nudgeText", today)
            if (resp2.isSuccessful) {

//                val jsonString = resp2.body().toString().trimIndent()
//                val jsonObjects = JSONObject(jsonString)
//                val jsonArray = jsonObjects.getJSONArray("data")
//                for (i in 0..jsonArray.length() - 1) {
//                    val iObject = jsonArray.getJSONObject(i)
//                    val userr = iObject.getString("user")
//                    val ur = iObject["user"]
//                    val aa = listOf(ur)
//                    Log.d("nudgeText", userr.javaClass.toString())
//                    Log.d("nudgeText", "================== ${i + 1} 번째==================")
//                    Log.d("nudgeText", "${i + 1}번 id : $userr")
//                    Log.d("nudgeText", "${i + 1}번 id : $ur")
//                }

                Log.d("nudgeText", resp2.body().toString())
                Log.d("nudgeText", resp2.body()?.data.toString())
                Log.d("nudgeText", resp2.body()?.merge?.get(0)?.user?.get(0)?.name.toString())
                Log.d("nudgeText", resp2.body()?.data?.get(0)?.user?.name.toString())


                val items = mutableListOf<SpaceId>()

                if(resp2.body()?.data == null){
                    Log.d("nudgeText", "space 없는 id / 나중에 혼자인 경우도 구현해야함")
                } else {

                    for (i in 0..(resp2.body()?.data?.size?.minus(1)!!)) {
                        val name = resp2.body()?.data?.get(i)?.user?.name.toString()
                        val emoji = resp2.body()?.data?.get(i)?.user?.emoji.toString()

                        items.add(SpaceId(name, emoji))
                    }
                    Log.d("nudgeText", items.toString())
                    Log.d("nudgeText", resp2.body()?.data?.size.toString())

                    val rv = binding.mainTestRV
                    val rvAdapter = RVMainAdapter(items)
                    // Only the original thread that created a view hierarchy can touch its views 에러 시 사용
                    runOnUiThread {
                        rv.adapter = rvAdapter
                        rv.layoutManager = LinearLayoutManager(this)
                    }

                    rvAdapter.itemClick = object : RVMainAdapter.ItemClick {
                        override fun onClick(view: View, position: Int) {
                            Toast.makeText(baseContext, items[position].name, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "space 정보 가져오기 실패하였습니다.", Toast.LENGTH_SHORT).show()
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