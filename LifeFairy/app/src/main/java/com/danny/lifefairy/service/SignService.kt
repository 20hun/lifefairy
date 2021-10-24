package com.danny.lifefairy.service

import com.danny.lifefairy.form.HTTP_GET_Model
import com.danny.lifefairy.form.PostModel
import com.danny.lifefairy.form.PostResult
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SignService {

    @POST("/api/users/register")
    @Headers("content-type: application/json")
    fun post_users(
        @Body jsonparams: PostModel
    ): Call<PostResult>

    @GET("/api/users/email-check")
    @Headers("accept: application/json",
        "content-type: application/json"
    )
    fun get_users(
    ): Call<HTTP_GET_Model>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "http://133.186.251.93/"

        fun create(): SignService {

            val gson :Gson =   GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(SignService::class.java)
        }
    }
}