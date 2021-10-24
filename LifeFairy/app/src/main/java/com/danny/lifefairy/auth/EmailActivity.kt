package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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

    val api = SignService.create()

    lateinit var questionEmail : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        emailBinding = ActivityEmailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.nextStepAuthEmailBtn.setOnClickListener {
//            api.get_users().enqueue(object : Callback<HTTP_GET_Model>{
//                override fun onResponse(call: Call<HTTP_GET_Model>, response: Response<HTTP_GET_Model>) {
//                    val result = response.body()
//                    Log.d("로그인", result.toString())
//                }
//
//                override fun onFailure(call: Call<HTTP_GET_Model>, t: Throwable) {
//                    Log.e("로그인", t.message.toString())
//                }
//            })
//            val data = PostModel(
//                ,
//                password.text.toString(),
//                name.text.toString(),
//                emoji.text.toString()
//            )

//            api.post_users(data).enqueue(object : Callback<PostResult> {
//                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
//                    Log.d("log222", response.toString())
//                    Log.d("log222", response.body().toString())
//                }
//
//                override fun onFailure(call: Call<PostResult>, t: Throwable) {
//                    // 실패
//                    Log.d("log222", t.message.toString())
//                    Log.d("log222", "fail")
//                }
//            })

            val intent = Intent(this, AuthEmailActivity::class.java)
            startActivity(intent)
        }

        questionEmail = findViewById(R.id.emailArea)

        val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        fun checkEmail() : Boolean {
            var email = questionEmail.text.toString().trim() //공백제거
            var validEmail = findViewById<TextView>(R.id.validEmail)
            val p = Pattern.matches(emailValidation, email) // 서로 패턴이 맞닝?
            if (p) {
                //이메일 형태가 정상일 경우
                validEmail.setText("")
                //questionEmail.setTextColor(-65536)
                return true
            } else {
                validEmail.setText("이메일 형식이 올바른지 확인해주세요.")
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

//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://133.186.251.93/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val service = retrofit.create(SignService::class.java)
//
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
//
//        nextStepAuthEmailBtn.setOnClickListener {
////            val email = findViewById<EditText>(R.id.emailArea)
////
////            service.emailCheck(email.text.toString()).enqueue(object :Callback<EmailCheckForm>{
////                override fun onResponse(call: Call<EmailCheckForm>, response: Response<EmailCheckForm>) {
////                    val result = response.body()
////                    Log.d("email check success", "${result}")
////                }
////
////                override fun onFailure(call: Call<EmailCheckForm>, t: Throwable) {
////                    Log.e("email check fail","${t.localizedMessage}")
////                }
////            })
////
////

//
//            val email = findViewById<EditText>(R.id.emailArea)
//            val password = findViewById<EditText>(R.id.passwordArea)
//            val name = findViewById<EditText>(R.id.nameArea)
//            val emoji = findViewById<EditText>(R.id.emojiArea)
//            service.signAccount(email.text.toString(), password.text.toString(), name.text.toString(), emoji.text.toString()).enqueue(object :Callback<String>{
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    val result = response.body()
//                    Log.d("succcccccccessssssss", "${result}")
//                }
//
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    Log.e("ffffffffaille","${t.localizedMessage}")
//                }
//            })
    }
}