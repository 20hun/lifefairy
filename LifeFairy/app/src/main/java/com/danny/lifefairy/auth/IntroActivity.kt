package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.danny.lifefairy.MainActivity
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityIntroBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.danny.lifefairy.form.PostGoogleModel
import com.danny.lifefairy.service.SignService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread


class IntroActivity : AppCompatActivity() {

    private var doubleBackToExit = false

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private var introBinding : ActivityIntroBinding? = null
    private val binding get() = introBinding!!
    private var spaceCheck = false

    companion object {
        private const val TAG = "IntroActivity"
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        introBinding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.emailLoginBtn.setOnClickListener {
            val intent = Intent(this, EmailActivity::class.java)
            startActivity(intent)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("992018839527-1rte0l0d0mkcljrdurhd3ib6p0bkju1v.apps.googleusercontent.com")
            .requestServerAuthCode("992018839527-1rte0l0d0mkcljrdurhd3ib6p0bkju1v.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        binding.googleLoginButton.setOnClickListener {
            signIn()
        }

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "????????? ?????? ???(?????? ??????)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "???????????? ?????? ???", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "?????? ????????? ???????????? ?????? ????????? ??? ?????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "?????? ???????????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "???????????? ?????? scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "????????? ???????????? ??????(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "?????? ?????? ????????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "?????? ??????", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Log.e(TAG, "????????? ????????? ??????")
                // kakao access token
                Log.d(TAG, "????????? access token" + token.accessToken)
                thread {
                    val api = SignService.tokenRequest(token.accessToken)
                    val resp = api.exchange_kakao_token().execute()
                    if (resp.isSuccessful) {
                        GlobalApplication.prefs.setString("accessToken", resp.body()?.access_token.toString())
                        GlobalApplication.prefs.setString("refreshToken", resp.body()?.refresh_token.toString())

                        thread {
                            val api2 = SignService.tokenRequest(GlobalApplication.prefs.getString("accessToken", ""))
                            val resp2 = api2.get_space_check().execute()
                            if (resp2.body()?.spaceYn.toString() == "Y"){
                                spaceCheck = true
                            }
                            // space ?????? ?????? ???????????? ???????????? ????????????
                            var intent = Intent(this, MainActivity::class.java)
                            if(!spaceCheck){
                                intent = Intent(this, InviteActivity::class.java)
                            }
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()
                        }
                    } else {
                        Log.d(TAG, "????????? ?????? ?????? ??????")
                    }
                }
            }
        }

        binding.kakaoLoginButton.setOnClickListener {
            Log.e("aaaaaa", "ddddd")
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                Log.e("aaaaaa", "ddddd1")
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
                Log.e("aaaaaa", "ddddd2")
            }else{
                Log.e("aaaaaa", "ddddd3")
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                Log.e("aaaaaa", "ddddd4")
            }
            Log.e("aaaaaa", "ddddd5")
        }
    }

    // ???????????? ????????? onBackPressed() ?????????
    override fun onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            Toast.makeText(this, "?????????????????? ??????????????? ?????? ??? ???????????????.", Toast.LENGTH_SHORT).show()
            doubleBackToExit = true
            runDelayed(1500L) {
                doubleBackToExit = false
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle1:" + account.email)
                Log.d(TAG, "firebaseAuthWithGoogle2:" + account.serverAuthCode)

                val api = SignService.create2()
                val data = PostGoogleModel(
                    "authorization_code",
                    "992018839527-1rte0l0d0mkcljrdurhd3ib6p0bkju1v.apps.googleusercontent.com",
                    getString(R.string.google_secret),
                    "https://localhost.com",
                    account.serverAuthCode
                )
                thread {
                    val resp = api.google_access_token(data).execute()
                    CoroutineScope(Dispatchers.Main).launch {
                        if (resp.isSuccessful) {
                            val googleAT = resp.body()?.access_token.toString()
                            thread {
                                val api2 = SignService.tokenRequest(googleAT)
                                val resp2 = api2.exchange_google_token().execute()
                                if (resp2.isSuccessful) {
                                    GlobalApplication.prefs.setString("accessToken", resp2.body()?.access_token.toString())
                                    GlobalApplication.prefs.setString("refreshToken", resp2.body()?.refresh_token.toString())
                                    Log.e(TAG, "?????? ?????? ?????? ??????")
                                } else {
                                    Log.e(TAG, "?????? ?????? ?????? ??????")
                                }
                            }
                        } else {
                            Log.d(TAG, "?????? ?????? ???????????? ??????")
                        }
                    }
                }

                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    thread {
                        val api = SignService.tokenRequest(GlobalApplication.prefs.getString("accessToken", ""))
                        val resp = api.get_space_check().execute()
                        if (resp.body()?.spaceYn.toString() == "Y"){
                            spaceCheck = true
                        }
                        var intent = Intent(this, MainActivity::class.java)
                        if(!spaceCheck){
                            intent = Intent(this, InviteActivity::class.java)
                        }
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    // updateUI(null)
                }
            }
    }

    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }

    override fun onDestroy() {
        introBinding = null
        super.onDestroy()
    }
}