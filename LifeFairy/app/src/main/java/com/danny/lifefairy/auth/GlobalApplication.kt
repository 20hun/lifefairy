package com.danny.lifefairy.auth

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()

        KakaoSdk.init(this, "d06ed40143902b1f05df2736de439a7e")
    }

}