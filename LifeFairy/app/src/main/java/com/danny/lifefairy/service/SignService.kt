package com.danny.lifefairy.service

import com.danny.lifefairy.form.*
import com.google.android.gms.auth.api.accounttransfer.AuthenticatorTransferCompletionStatus
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
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

    @POST("/api/users/confirm")
    @Headers("content-type: application/json")
    fun post_email_auth(
        @Body jsonparams: PostEmailAuthModel
    ): Call<PostResult>

    @POST("/api/users/email-check")
    @Headers("content-type: application/json")
    fun post_email_check(
        @Body jsonparams: PostEmailCheckModel
    ): Call<PostResult>

    @POST("/api/users/login")
    @Headers("content-type: application/json")
    fun post_login(
        @Body jsonparams: PostLoginModel
    ): Call<PostResult>

    @GET("/api/spaces/check-space")
    fun get_space_check(
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
        fun tokenRequest(token : String): SignService {

            val gson :Gson =   GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(OkHttpClient.Builder().addInterceptor { chain ->
                    val request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${token}").build()
                    chain.proceed(request)}.build())
                .build()
                .create(SignService::class.java)
        }
    }
}