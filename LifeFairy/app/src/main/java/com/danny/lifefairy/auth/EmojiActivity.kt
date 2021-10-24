package com.danny.lifefairy.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danny.lifefairy.R

class EmojiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val nickname = intent.getStringExtra("nickname")
        // Toast.makeText(this, email, Toast.LENGTH_LONG).show()

        val emojis = mutableListOf<String>()
        emojis.add("\uD83D\uDC36")
        emojis.add("\uD83E\uDD8A")
        emojis.add("\uD83E\uDD84")
        emojis.add("\uD83D\uDC37")
        emojis.add("\uD83D\uDC39")
        emojis.add("\uD83D\uDC30")
        emojis.add("\uD83D\uDC3B")
        emojis.add("\uD83D\uDC3C")
        emojis.add("\uD83D\uDC38")

        val emojiRV = findViewById<RecyclerView>(R.id.emojiRV)
        val emojiRVAdapter = EmojiRVAdapter(emojis)
        emojiRV.adapter = emojiRVAdapter
        emojiRV.layoutManager = LinearLayoutManager(this)

        emojiRVAdapter.itemClick = object : EmojiRVAdapter.ItemClick {
            override fun onClick(view : View, position : Int) {
                Toast.makeText(baseContext, emojis[position], Toast.LENGTH_LONG).show()
            }
        }
    }
}