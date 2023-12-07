package com.example.pong

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pong.databinding.FragmentSaveBinding

class SaveFragment: Fragment() {

    lateinit var binder:  FragmentSaveBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binder = FragmentSaveBinding.inflate(layoutInflater, container, false )

        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binder.saveBtn.setOnClickListener {

            val name: String = binder.saveName.text.toString()
            val score: Int = requireArguments().getInt("Score")

            if (name.isEmpty()){

                Toast.makeText(requireContext(), "Name is missing", Toast.LENGTH_SHORT).show()
            }
            else {
               // println(score)
                DataManager.createPlayer(name, score)
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)

            }
        }

        binder.cancelBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

    }
}