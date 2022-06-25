package com.example.toilet_seoul

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

var toiletNm: String? = null
var rdnmadr: String? = null
var lnmadr: String? = null
var unisexToiletYn: String? = null
var menToiletBowlNumber: String? = null
var menUrineNumber: String? = null
var menHandicapToiletBowlNumber: String? = null
var menHandicapUrinalNumber: String? = null
var menChildrenToiletBowlNumber: String? = null
var menChildrenUrinalNumber: String? = null
var ladiesToiletBowlNumber: String? = null
var ladiesHandicapToiletBowlNumber: String? = null
var ladiesChildrenToiletBowlNumber: String? = null
var phoneNumber: String? = null
var openTime: String? = null
var position: String? = null
var emgBellYn: String? = null
var enterentCctvYn: String? = null
var dipersExchgPosi: String? = null

class BottomSheet : BottomSheetDialogFragment() {

    val bottomSheetBehavior = view?.let {
        BottomSheetBehavior.from(
            it.findViewById<LinearLayout>(R.id.bottomSheet)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toiletNm = arguments?.getString("toiletNm")
        rdnmadr = arguments?.getString("rdnmadr")
        lnmadr = arguments?.getString("lnmadr")
        unisexToiletYn = arguments?.getString("unisexToiletYn")
        menToiletBowlNumber = arguments?.getString("menToiletBowlNumber")
        menUrineNumber = arguments?.getString("menUrineNumber")
        menHandicapToiletBowlNumber = arguments?.getString("menHandicapToiletBowlNumber")
        menHandicapUrinalNumber = arguments?.getString("menHandicapUrinalNumber")
        menChildrenToiletBowlNumber = arguments?.getString("menChildrenToiletBowlNumber")
        menChildrenUrinalNumber = arguments?.getString("menChildrenUrinalNumber")
        ladiesToiletBowlNumber = arguments?.getString("ladiesToiletBowlNumber")
        ladiesHandicapToiletBowlNumber = arguments?.getString("ladiesHandicapToiletBowlNumber")
        ladiesChildrenToiletBowlNumber = arguments?.getString("ladiesChildrenToiletBowlNumber")
        phoneNumber = arguments?.getString("phoneNumber")
        openTime = arguments?.getString("openTime")
        position = arguments?.getString("position")
        emgBellYn = arguments?.getString("emgBellYn")
        enterentCctvYn = arguments?.getString("enterentCctvYn")
        dipersExchgPosi = arguments?.getString("dipersExchgPosi")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?):Dialog{
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        //setting layout with bottom sheet
        bottomSheet.setContentView(R.layout.bottom_sheet)

        bottomSheet.behavior.setPeekHeight(800)

        bottomSheet.behavior.addBottomSheetCallback(object : BottomSheetCallback() {
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
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<ImageButton>(R.id.cancelBtn)?.setOnClickListener {
            dismiss()
        }

        //비상연락 버튼클릭이벤트 - DangerCall
        view?.findViewById<FloatingActionButton>(R.id.SOSbtn)?.setOnClickListener {
            val intent = Intent(context, DangerCall::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        hideAppBar()
        bottomSheetBehavior?.state ?: BottomSheetBehavior.STATE_COLLAPSED

        view?.findViewById<TextView>(R.id.name)?.text = toiletNm
        view?.findViewById<TextView>(R.id.address)?.text = lnmadr
        view?.findViewById<TextView>(R.id.phone_number)?.text = phoneNumber
        view?.findViewById<TextView>(R.id.open_time)?.text = openTime

        view?.findViewById<TextView>(R.id.toiletNm)?.text = "화장실명: $toiletNm"
        view?.findViewById<TextView>(R.id.rdnmadr)?.text = "소재지도로명주소: $rdnmadr"
        view?.findViewById<TextView>(R.id.lnmadr)?.text = "소재지지번주소: $lnmadr"
        view?.findViewById<TextView>(R.id.unisexToiletYn)?.text = "남녀공용화장실여부: $unisexToiletYn"
        view?.findViewById<TextView>(R.id.menToiletBowlNumber)?.text = "남성용-대변기수: $menToiletBowlNumber"
        view?.findViewById<TextView>(R.id.menUrineNumber)?.text = "남성용-소변기수: $menUrineNumber"
        view?.findViewById<TextView>(R.id.menHandicapToiletBowlNumber)?.text = "남성용-장애인용대변기수: $menHandicapToiletBowlNumber"
        view?.findViewById<TextView>(R.id.menHandicapUrinalNumber)?.text = "남성용-장애인용소변기수: $menHandicapUrinalNumber"
        view?.findViewById<TextView>(R.id.menChildrenToiletBowlNumber)?.text = "남성용-어린이용대변기수: $menChildrenToiletBowlNumber"
        view?.findViewById<TextView>(R.id.menChildrenUrinalNumber)?.text = "남성용-어린이용소변기수: $menChildrenUrinalNumber"
        view?.findViewById<TextView>(R.id.ladiesToiletBowlNumber)?.text = "여성용-대변기수: $ladiesToiletBowlNumber"
        view?.findViewById<TextView>(R.id.ladiesHandicapToiletBowlNumber)?.text = "여성용-장애인용대변기수: $ladiesHandicapToiletBowlNumber"
        view?.findViewById<TextView>(R.id.ladiesChildrenToiletBowlNumber)?.text = "여성용-어린이용대변기수: $ladiesChildrenToiletBowlNumber"
        view?.findViewById<TextView>(R.id.phoneNumber)?.text = "전화번호: $phoneNumber"
        view?.findViewById<TextView>(R.id.openTime)?.text = "개방시간: $openTime"
        view?.findViewById<TextView>(R.id.position)?.text = "위치(좌표): $position"
        view?.findViewById<TextView>(R.id.emgBellYn)?.text = "비상벨설치: $emgBellYn"
        view?.findViewById<TextView>(R.id.enterentCctvYn)?.text = "화장실입구CCTV설치유무: $enterentCctvYn"
        view?.findViewById<TextView>(R.id.dipersExchgPosi)?.text = "기저귀교환대장소: $dipersExchgPosi"
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