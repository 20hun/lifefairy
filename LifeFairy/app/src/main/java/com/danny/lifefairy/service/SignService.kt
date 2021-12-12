package com.danny.lifefairy.service

import com.danny.lifefairy.form.*
import com.danny.lifefairy.form.json.spaceInfo.SpaceStatusUser
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.FieldMap

import retrofit2.http.POST

import retrofit2.http.FormUrlEncoded







interface SignService {

    @POST("/api/users/register")
    @Headers("content-type: application/json")
    fun post_users(
        @Body jsonparams: PostModel
    ): Call<TokenResult>

    @POST("/api/users/confirm")
    @Headers("content-type: application/json")
    fun post_email_auth(
        @Body jsonparams: PostEmailAuthModel
    ): Call<TokenResult>

    @POST("/api/users/email-check")
    @Headers("content-type: application/json")
    fun post_email_check(
        @Body jsonparams: PostEmailCheckModel
    ): Call<TokenResult>

    @POST("/api/users/login")
    @Headers("content-type: application/json")
    fun post_login(
        @Body jsonparams: PostLoginModel
    ): Call<TokenResult>

    @POST("/api/notifications/device")
    @Headers("content-type: application/json")
    fun device_token_post(
        @Body jsonparams: PostDeviceTokenModel
    ): Call<DevicePostData>

    @POST("/api/spaces/check-invitationcode")
    fun check_invitation_code(
        @Body jsonparams: InvitationData
    ): Call<PostInvitationCheck>

    @POST("/api/spaces/participate")
    fun join_space(
        @Body jsonparams: JoinSpaceData
    ): Call<DevicePostData>

    @GET("/api/spaces/check-space")
    fun get_space_check(
    ): Call<HTTP_GET_Model>

    @GET("/api/nudges")
    fun get_nudge_text(
    ): Call<NudgeModel>

    @GET("/api/todos/status/{date}")
    fun get_space_user(
        @Path("date") date : String?
    ): Call<SpaceStatusUser>

    @POST("/api/users/kakao")
    fun exchange_kakao_token(
    ): Call<TokenResult>

    @POST("/api/users/token")
    fun from_refresh_get_access(
    ): Call<TokenData>

    @POST("/api/spaces/invite")
    fun invite_code_create(
    ): Call<InvitationData>

    @POST("/api/users/google")
    fun exchange_google_token(
    ): Call<TokenResult>

    @POST("/token")
    fun google_access_token(
        @Body jsonparams: PostGoogleModel
    ) : Call<GoogleData>

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

        fun create2(): SignService {

            val gson :Gson =   GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl("https://oauth2.googleapis.com")
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(SignService::class.java)
        }
    }
}