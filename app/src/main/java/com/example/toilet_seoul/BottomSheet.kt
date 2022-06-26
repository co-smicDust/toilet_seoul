package com.example.toilet_seoul

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_SENDTO
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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

var latlng: LatLng? = null

class BottomSheet : BottomSheetDialogFragment() {

    val bottomSheetBehavior = view?.let {
        BottomSheetBehavior.from(
            it.findViewById<LinearLayout>(R.id.bottomSheet)
        )
    }

    //비상 연락망 관련
    private var inputPhoneNum: String? = null

    private val adapter: ContactAdapter by lazy {
        ContactAdapter({ contact ->
            inputPhoneNum = contact.number
            if (inputPhoneNum != null) {
                val myUri = Uri.parse("smsto:${inputPhoneNum}") //데이터베이스 연결 전 임의로
                val myIntent = Intent(ACTION_SENDTO, myUri)
                // 문자 전송 화면 이동 시 미리 문구를 적어서 보내기
                // myIntent를 가지고 갈 때 -> putExtra로 데이터를 담아서 보내자
                myIntent.putExtra("sms_body", "위급 상황입니다.")
                startActivity(myIntent)
            }
        }, { contact ->
            inputPhoneNum = contact.number
            if(inputPhoneNum != null){
                val myUri = Uri.parse("tel:${inputPhoneNum}")
                val myIntent = Intent(ACTION_DIAL, myUri)
                startActivity(myIntent)
            }
        })
    }

    //리뷰 관련
    val database: DatabaseReference = Firebase.database.reference
    private val currentUser = Firebase.auth.currentUser
    private val toiletRef = database.child("toilet")
    private val reviewRef = database.child("toilet").child(R.id.toiletNum.toString()).child("review")

    //var curUID = currentUser?.uid.toString()
    var curUID = "지영 테스트"

    private var userNm: String? = null
    private var ratingStar: Double? = null
    private var contentR: String? = null

    private lateinit var reviewAdapter: ReviewAdapter
    private var newsList: ArrayList<Review> = ArrayList()

    @SuppressLint("CutPasteId")
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

        latlng = arguments?.getParcelable("latlng") as LatLng?

        val contactViewModel: ContactViewModel =
            ViewModelProvider(this, ContactViewModel.Factory(requireActivity().application)).get(ContactViewModel::class.java)
        contactViewModel.getAll().observe(this, Observer<List<Contact>> { contacts ->
            adapter.setContacts(contacts!!)
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?):Dialog{
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        //setting layout with bottom sheet
        bottomSheet.setContentView(R.layout.bottom_sheet)

        bottomSheet.behavior.peekHeight = 800

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
    @SuppressLint("CutPasteId")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //리뷰 바텀시트 하단에 불러오기(아래 Unresolved Ref들은 레이아웃에 아이디 추가해야 함)
        reviewRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //userNm: 사용자 닉네임
                userNm = snapshot.child(curUID).key
                //ratingStar: 별점
                ratingStar = snapshot.child(curUID).child("rate").value as Double?
                //contentR: 리뷰내용
                contentR = snapshot.child(curUID).child("content").value.toString()

                val review = Review(userNm.toString(), ratingStar?.toFloat(), contentR, R.drawable.ic_personal) //사용자의 이미지(프사) 추가?
                if (!newsList.contains(review))
                    newsList.add(review)

                reviewAdapter = ReviewAdapter(context!!, newsList)
                view?.findViewById<RecyclerView>(R.id.recyclerview)?.layoutManager = LinearLayoutManager(context)
                view?.findViewById<RecyclerView>(R.id.recyclerview)?.adapter = reviewAdapter
                view?.findViewById<RecyclerView>(R.id.recyclerview)?.setHasFixedSize(true)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        // x를 누르면 dialog 닫힘
        view?.findViewById<ImageButton>(R.id.cancelBtn)?.setOnClickListener {
            dismiss()
        }

        //비상연락 버튼클릭이벤트 - DangerCall
        view?.findViewById<FloatingActionButton>(R.id.SOSbtn)?.setOnClickListener {
            showDialog()
        }

        //리뷰 쓰기
        view?.findViewById<Button>(R.id.confirmButton)?.setOnClickListener {
            val contentText = view?.findViewById<EditText>(R.id.contentText)?.text
            val rate: Float = view?.findViewById<RatingBar>(R.id.ratingBar)?.rating!!.toFloat()

            if (contentText.toString() != "" && rate > 0){
                //toiletNum-review에 리뷰 저장

                reviewRef.child(curUID).child("rate").setValue(rate)
                reviewRef.child(curUID).child("content").setValue(contentText.toString())

                Toast.makeText(context, "리뷰가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                contentText?.clear()
                view?.findViewById<RatingBar>(R.id.ratingBar)?.rating = 0.0F
            } else {
                Toast.makeText(context, "별점이나 리뷰를 작성해주세요.", Toast.LENGTH_SHORT).show()
            }
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

    fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("짧게 누르면 문자, 길게 누르면 전화")
        val customLayout: View = layoutInflater.inflate(R.layout.alert_dialog, null)
        builder.setView(customLayout)
        builder.setNegativeButton("닫기", DialogInterface.OnClickListener { dialog, i ->
            dialog.dismiss()
        })
        val alertDialog = builder.create()
        val recyclerView: RecyclerView =
            customLayout.findViewById(R.id.recyclerView)
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter
        alertDialog.show()
    }
}