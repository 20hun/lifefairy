package com.danny.lifefairy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danny.lifefairy.R
import com.danny.lifefairy.databinding.ActivityEmojiBinding

class EmojiActivity : AppCompatActivity() {

    private var emojiBinding : ActivityEmojiBinding? = null
    private val binding get() = emojiBinding!!

    var emoji = ""
    var checkNextPage = false

    lateinit var nextBtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        emojiBinding = ActivityEmojiBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val nickname = intent.getStringExtra("nickname")

        nextBtn = findViewById(R.id.nextStepAuthBtn)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.nextStepAuthBtn.setOnClickListener {
            if (checkNextPage) {
                val intent = Intent(this, AuthEmailActivity::class.java)
                intent.putExtra("emoji", emoji)
                //intent.putExtra("email", "문자열 전달")
                startActivity(intent)
            }
        }

    }

    fun changeEmoji(fragmentEmoji : String) {
        emoji = fragmentEmoji
        checkNextPage = true
        nextBtn.setImageResource(R.drawable.right_click_bg_black_r)
    }
}