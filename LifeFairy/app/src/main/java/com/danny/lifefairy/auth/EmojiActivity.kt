package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityEmojiBinding
import com.danny.lifefairy.form.HTTP_GET_Model
import com.danny.lifefairy.form.PostModel
import com.danny.lifefairy.form.TokenResult
import com.danny.lifefairy.service.SignService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmojiActivity : AppCompatActivity() {

    private var emojiBinding : ActivityEmojiBinding? = null
    private val binding get() = emojiBinding!!

    val api = SignService.create()

    var emoji = ""
    var checkNextPage = false

    var authEmailCode = ""

    lateinit var nextBtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        emojiBinding = ActivityEmojiBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val name = intent.getStringExtra("name")

        nextBtn = findViewById(R.id.nextStepAuthBtn)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.nextStepAuthBtn.setOnClickListener {
            if (checkNextPage) {
//                api.get_users().enqueue(object : Callback<HTTP_GET_Model> {
//                    override fun onResponse(call: Call<HTTP_GET_Model>, response: Response<HTTP_GET_Model>) {
//                        val result = response.body()
//                        Log.d("로그인", result.toString())
//                    }
//
//                    override fun onFailure(call: Call<HTTP_GET_Model>, t: Throwable) {
//                        Log.e("로그인", t.message.toString())
//                    }
//                })

                val data = PostModel(
                    email,
                    password,
                    name,
                    emoji
                )
                Log.d("log222", emoji)

                api.post_users(data).enqueue(object : Callback<TokenResult> {
                    override fun onResponse(call: Call<TokenResult>, response: Response<TokenResult>) {
                        Log.d("log222", response.toString())
                        Log.d("log222", response.body().toString())
                    }

                    override fun onFailure(call: Call<TokenResult>, t: Throwable) {
                        // 실패
                        Log.d("log222", t.message.toString())
                        Log.d("log222", "fail")
                    }
                })

                val intent = Intent(this, AuthEmailActivity::class.java)
                intent.putExtra("email", email)
                //intent.putExtra("email", "문자열 전달")
                startActivity(intent)
            }
        }
    }

    // fragment로부터 데이터 받아오는 함수
    fun changeEmoji(fragmentEmoji : String) {
        emoji = fragmentEmoji
        checkNextPage = true
        nextBtn.setImageResource(R.drawable.right_click_bg_black_r)
    }
}