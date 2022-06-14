package com.example.toilet_seoul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.toilet_seoul.databinding.FilterSearchBinding


class ThirdFragment : Fragment(), MainActivity.onBackPressedListener {

    private lateinit var binding: FilterSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FilterSearchBinding.inflate(layoutInflater)
        val view: View = binding.root
        return view
    }
    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        //requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.clear)?.setOnClickListener {
            if (binding.star4.isChecked) binding.star4.isChecked = false
            if (binding.star3.isChecked) binding.star3.isChecked = false
            if (binding.star2.isChecked) binding.star2.isChecked = false
            if (binding.starUnder.isChecked) binding.starUnder.isChecked = false
            if (binding.distance200.isChecked) binding.distance200.isChecked = false
            if (binding.distance500.isChecked) binding.distance500.isChecked = false
            if (binding.notUnisex.isChecked) binding.notUnisex.isChecked = false
            if (binding.disabled.isChecked) binding.disabled.isChecked = false
            if (binding.diaper.isChecked) binding.diaper.isChecked = false
            if (binding.withChildren.isChecked) binding.withChildren.isChecked = false
            if (binding.feminineProducts.isChecked) binding.feminineProducts.isChecked = false
            if (binding.cctv.isChecked) binding.cctv.isChecked = false

        }
    }
}