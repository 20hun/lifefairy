package com.danny.lifefairy.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.danny.lifefairy.MainActivity
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityInviteBinding
import com.danny.lifefairy.form.InvitationData
import com.danny.lifefairy.form.JoinSpaceData
import com.danny.lifefairy.service.SignService
import kotlin.concurrent.thread

class InviteActivity : AppCompatActivity() {

    private var inviteBinding : ActivityInviteBinding? = null
    private val binding get() = inviteBinding!!

    private val TAG = "InviteActivity"
    private val api = SignService.tokenRequest(GlobalApplication.prefs.getString("accessToken", ""))

    lateinit var inviteCode : TextView

    private var checkNextPage = false

    // 0: 잘못된 초대코드, 1: 올바른 초대코드
    private var invitationPopup = 0

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inviteBinding = ActivityInviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noneInviteCode.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        inviteCode = binding.inviteCodeArea

        fun checkInviteCode() {
            var code = inviteCode.text.toString().trim()
            var validBtn = binding.nextStepBtn

            if (code.isNotEmpty()) {
                validBtn.setImageResource(R.drawable.right_click_bg_black_r)
                checkNextPage = true
            } else {
                validBtn.setImageResource(R.drawable.right_click_bg_black)
                checkNextPage = false
            }
        }

        inviteCode.addTextChangedListener(object : TextWatcher {
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
                checkInviteCode()
            }
        })

        binding.nextStepBtn.setOnClickListener {
            if (checkNextPage) {

                val data = InvitationData(
                    binding.inviteCodeArea.text.toString()
                )

                val mDialogView = LayoutInflater.from(this).inflate(R.layout.invite_dialog, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                val title = mAlertDialog.findViewById<TextView>(R.id.titlePopup)
                val context = mAlertDialog.findViewById<TextView>(R.id.contextPopup)
                val wrongInvite = mAlertDialog.findViewById<ImageView>(R.id.wrongInviteCode)
                val emoji = mAlertDialog.findViewById<TextView>(R.id.inviterEmoji)

                val btn1 = mAlertDialog.findViewById<Button>(R.id.noInviter)
                val btn2 = mAlertDialog.findViewById<Button>(R.id.yesInviter)
                val btn3 = mAlertDialog.findViewById<Button>(R.id.okayBtn)
                var spaceId = ""

                thread {
                    val resp = api.check_invitation_code(data).execute()
                    if (resp.isSuccessful){
                        Log.e(TAG, resp.body().toString())
                        if(!resp.body()?.spaceId.toString().isEmpty()){
                            // 올바른 초대코드
                            invitationPopup = 1
                            val inviterName = resp.body()?.inviterName.toString()
                            runOnUiThread {
                                spaceId = resp.body()?.spaceId.toString()
                                title?.text = "초대한 사람: $inviterName 님"
                                context?.text = "나를 초대한 사람이 맞나요?"
                                emoji?.text = resp.body()?.inviteEmoji.toString()
                                btn1?.setVisibility(View.VISIBLE)
                                btn2?.setVisibility(View.VISIBLE)
                            }
                        }
                    } else {
                        invitationPopup = 0
                        runOnUiThread {
                            title?.text = "앗! 다시 한 번 확인해주세요."
                            context?.text = "잘못된 초대 코드가 입력되어,\n메이트에게 새 코드를 받아 다시 시도해보세요."
                            wrongInvite?.setImageResource(R.drawable.wrong_invite_code)
                            btn3?.setVisibility(View.VISIBLE)
                        }
                        Log.e(TAG, "잘못된 초대 코드")
                    }
                }

                btn1?.setOnClickListener {
                    mAlertDialog.dismiss()
                }
                btn3?.setOnClickListener {
                    mAlertDialog.dismiss()
                }
                btn2?.setOnClickListener {

                    thread {
                        val data2 = JoinSpaceData(
                            spaceId,
                            binding.inviteCodeArea.text.toString()
                        )

                        val resp2 = api.join_space(data2).execute()
                        if (resp2.isSuccessful){
                            if (resp2.body()?.message.toString() == "성공"){
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                finish()
                                mAlertDialog.dismiss()
                            }
                        } else {
                            Log.e(TAG, "스페이스 가입 실패")
                        }
                    }
                }
            }
        }
    }
}