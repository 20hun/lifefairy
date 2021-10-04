package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.danny.lifefairy.R
import com.danny.lifefairy.form.EmailCheckForm
import com.danny.lifefairy.form.RegistForm
import kotlinx.android.synthetic.main.activity_email.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

class EmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://133.186.251.93/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(SignService::class.java)

        nextStepAuthEmailBtn.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailArea)
            val password = findViewById<EditText>(R.id.passwordArea)
            val name = findViewById<EditText>(R.id.nameArea)
            val emoji = findViewById<EditText>(R.id.emojiArea)

            service.signAccount(
                email.text.toString(),
                password.text.toString(),
                name.text.toString(),
                emoji.text.toString()
            ).enqueue(object : Callback<RegistForm> {
                override fun onResponse(call: Call<RegistForm>, response: Response<RegistForm>) {
                    val result = response.body()
                    Log.d("로그인", "${result}")
                }

                override fun onFailure(call: Call<RegistForm>, t: Throwable) {
                    Log.e("로그인", "${t.localizedMessage}")
                }
            })
        }

//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://133.186.251.93/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val service = retrofit.create(SignService::class.java)
//
//        backBtn.setOnClickListener {
//            onBackPressed()
//        }
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
////            val intent = Intent(this, AuthEmailActivity::class.java)
////            startActivity(intent)
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

interface SignService {
    @FormUrlEncoded
    @GET("api/users/email-check")
    fun emailCheck(@Field("email") email:String) : Call<EmailCheckForm>

    @FormUrlEncoded
    @POST("api/users/register")
    fun signAccount(@Field("email") email:String, @Field("password") password:String, @Field("name") name:String, @Field("emoji") emoji:String) : Call<RegistForm>
}