package com.danny.lifefairy.auth

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "dd5f1e1a09e6ef1f2b205938cba77ebb")
    }

}