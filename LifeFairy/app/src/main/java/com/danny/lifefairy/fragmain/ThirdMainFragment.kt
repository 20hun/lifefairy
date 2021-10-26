package com.danny.lifefairy.fragmain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.danny.lifefairy.R

class ThirdMainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_third_main, container, false)

        view.findViewById<TextView>(R.id.seeeBtn).setOnClickListener {
            it.findNavController().navigate(R.id.action_thirdMainFragment_to_firstMainFragment)
        }
        view.findViewById<TextView>(R.id.jhhhBtn).setOnClickListener {
            it.findNavController().navigate(R.id.action_thirdMainFragment_to_secondMainFragment)
        }

        return view
    }

}