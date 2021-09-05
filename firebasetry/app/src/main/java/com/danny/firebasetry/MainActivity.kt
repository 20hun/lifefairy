package com.danny.firebasetry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://133.186.251.93/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(SignService::class.java)

        val joinBtn = findViewById<Button>(R.id.joinBtn)
        joinBtn.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailArea)
            val password = findViewById<EditText>(R.id.passwordArea)
            val name = findViewById<EditText>(R.id.nameArea)

            service.login(email.text.toString(), password.text.toString(), name.text.toString()).enqueue(object :Callback<RegistForm2>{
                override fun onResponse(call: Call<RegistForm2>, response: Response<RegistForm2>) {
                    val result = response.body()
                    Log.d("로그인", "${result}")
                }

                override fun onFailure(call: Call<RegistForm2>, t: Throwable) {
                    Log.e("로그인","${t.localizedMessage}")
                }
            })
        }
    }
}

interface SignService {
    @FormUrlEncoded
    @POST("api/users/register")
    fun login(@Field("email") email:String, @Field("password") password:String, @Field("name") name:String) : Call<RegistForm2>
}