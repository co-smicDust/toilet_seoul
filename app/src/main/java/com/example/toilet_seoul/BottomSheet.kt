package com.example.toilet_seoul

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RatingBar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class BottomSheet : BottomSheetDialogFragment() {

    val bottomSheetBehavior = view?.let {
        BottomSheetBehavior.from(
            it.findViewById<LinearLayout>(R.id.bottomSheet)
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?):Dialog{
        var bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        //setting layout with bottom sheet
        bottomSheet.setContentView(R.layout.bottom_sheet)

        bottomSheet.behavior.setPeekHeight(800)

        bottomSheet.behavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(view: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED,
                    -> {
                        showView(view.findViewById<AppBarLayout>(R.id.appBarLayout), getActionBarSize())
                    }
                    BottomSheetBehavior.STATE_COLLAPSED,
                    -> {
                        hideAppBar()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> dismiss()
                    else -> {}
                }
            }

            override fun onSlide(view: View, slideOffset: Float) {

            }
        })

        return bottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }


    //button clicked
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<ImageButton>(R.id.cancelBtn)?.setOnClickListener {
            dismiss()
        }

        //비상연락 버튼클릭이벤트 - DangerCall
        view?.findViewById<FloatingActionButton>(R.id.SOSbtn)?.setOnClickListener {
            val intent = Intent(getContext(), DangerCall::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        hideAppBar()
        bottomSheetBehavior?.state ?: BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun hideAppBar() {
        val appbar: AppBarLayout? = view?.findViewById(R.id.appBarLayout)
        val params = appbar?.layoutParams
        params?.height = 0
        appbar?.layoutParams = params
    }

    private fun showView(view: View, size: Int) {
        val params = view.layoutParams
        params.height = size
        view.layoutParams = params
    }

    private fun getActionBarSize(): Int {
        val array =
            requireContext().theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        return array.getDimension(0, 0f).toInt()
    }

}