package com.danny.lifefairy.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.danny.lifefairy.R


class FirstEmojiFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_first_emoji, container, false)

        view.findViewById<Button>(R.id.refreshBtn).setOnClickListener {
            it.findNavController().navigate(R.id.action_firstEmojiFragment_to_secondEmojiFragment)
        }

        return view
    }

}