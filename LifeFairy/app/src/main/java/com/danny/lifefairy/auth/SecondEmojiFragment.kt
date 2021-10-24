package com.danny.lifefairy.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danny.lifefairy.R

class SecondEmojiFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_second_emoji, container, false)

        val emojis: List<String> = listOf("🌶","🍑","🍋","🍒","🥑","🥒","🥔","🍄","🍇")
        val emojisRV = mutableListOf<String>()
        for(emoji in emojis){
            emojisRV.add(emoji)
        }

        val emojiRV = view.findViewById<RecyclerView>(R.id.emojiRV)
        val emojiRVAdapter = EmojiRVAdapter(emojisRV)
        emojiRV.adapter = emojiRVAdapter
        emojiRV.layoutManager = LinearLayoutManager(context)

        emojiRVAdapter.itemClick = object : EmojiRVAdapter.ItemClick {
            override fun onClick(view : View, position : Int) {
                Toast.makeText(context, emojisRV[position], Toast.LENGTH_LONG).show()

                (activity as EmojiActivity).changeEmoji(emojisRV[position])
            }
        }

        view.findViewById<Button>(R.id.refreshBtn).setOnClickListener {
            it.findNavController().navigate(R.id.action_secondEmojiFragment_to_thirdEmojiFragment)
        }

        // Inflate the layout for this fragment
        return view
    }

}