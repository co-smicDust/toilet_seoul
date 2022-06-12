package com.example.toilet_seoul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.filter_search.*

class ThirdFragment : Fragment(), MainActivity.onBackPressedListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.filter_search, container, false)
    }
    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        //requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.findViewById<Button>(R.id.clear)?.setOnClickListener {
            if (star4.isChecked) star4.isChecked = false
            if (star3.isChecked) star3.isChecked = false
            if (star2.isChecked) star2.isChecked = false
            if (star_under.isChecked) star_under.isChecked = false
            if (distance200.isChecked) distance200.isChecked = false
            if (distance500.isChecked) distance500.isChecked = false
            if (not_unisex.isChecked) not_unisex.isChecked = false
            if (disabled.isChecked) disabled.isChecked = false
            if (diaper.isChecked) diaper.isChecked = false
            if (with_children.isChecked) with_children.isChecked = false
            if (feminine_products.isChecked) feminine_products.isChecked = false
            if (cctv.isChecked) cctv.isChecked = false
        }
    }
}