package com.example.toilet_seoul

import android.R
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class FirstFragment : Fragment(), MainActivity.onBackPressedListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true)
        //(activity as AppCompatActivity).supportActionBar?.title = "My Title"
        return inflater.inflate(com.example.toilet_seoul.R.layout.personal_information, container, false)
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        //requireActivity().supportFragmentManager.popBackStack()
    }
}