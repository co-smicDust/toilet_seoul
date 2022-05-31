package com.example.toilet_seoul

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap


class MapFragment : Fragment(), OnMapReadyCallback {
    var rootView: View? = null
    var mapView: MapView? = null

    // 런타임에서 권한이 필요한 퍼미션 목록
    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // 퍼미션 승인 요청시 사용하는 요청 코드
    val REQUEST_PERMISSION_CODE = 1

    // 기본 맵 줌 레벨
    val DEFAULT_ZOOM_LEVEL = 17f

    // 현재위치를 가져올수 없는 경우 서울 시청의 위치로 지도를 보여주기 위해 서울시청의 위치를 변수로 선언
    // 일단 과천청사역 1번출구로 바꿔둠
    // LatLng 클래스는 위도와 경도를 가지는 클래스
    val CITY_HALL = LatLng(37.4272309, 126.99090478)

    // 구글 맵 객체를 참조할 멤버 변수
    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        rootView = inflater.inflate(R.layout.map_view, container, false)
        mapView = rootView!!.findViewById<View>(R.id.mapView) as MapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<FloatingActionButton>(R.id.myLocationButton)?.setOnClickListener {
            when {
                hasPermissions() -> googleMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(getMyLocation(), DEFAULT_ZOOM_LEVEL)
                )
                else -> Toast.makeText(requireContext().applicationContext, "위치사용권한 설정에 동의해주세요", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onResume() {
        mapView!!.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(this.requireActivity())

        // 앱이 실행될때 런타임에서 위치 서비스 관련 권한체크
        if (hasPermissions()) {
            // 권한이 있는 경우 맵 초기화
            initMap()
        } else {
            // 권한 요청
            requestPermissions(PERMISSIONS, REQUEST_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 맵 초기화
        initMap()
    }

    // 앱에서 사용하는 권한이 있는지 체크하는 함수
    fun hasPermissions(): Boolean {
        // 퍼미션목록중 하나라도 권한이 없으면 false 반환
        for (permission in PERMISSIONS) {
            if (context?.let {
                    ActivityCompat.checkSelfPermission(
                        it?.applicationContext,
                        permission
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }


    // 맵 초기화하는 함수
    @SuppressLint("MissingPermission")
    fun initMap() {
        // 맵뷰에서 구글 맵을 불러오는 함수. 컬백함수에서 구글 맵 객체가 전달됨
        mapView?.getMapAsync {
            // 구글맵 멤버 변수에 구글맵 객체 저장
            googleMap = it
            // 현재위치로 이동 버튼 비활성화
            it.uiSettings.isMyLocationButtonEnabled = false
            // 위치 사용 권한이 있는 경우
            when {
                hasPermissions() -> {
                    // 현재위치 표시 활성화
                    it.isMyLocationEnabled = true
                    // 현재위치로 카메라 이동
                    it.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            getMyLocation(),
                            DEFAULT_ZOOM_LEVEL
                        )
                    )
                }
                else -> {
                    // 권한이 없으면 서울시청의 위치로 이동
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_HALL, DEFAULT_ZOOM_LEVEL))
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getMyLocation(): LatLng {
        // 위치를 측정하는 프로바이더를 GPS 센서로 지정
        val locationProvider: String = LocationManager.GPS_PROVIDER
        // 위치 서비스 객체를 불러옴
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 마지막으로 업데이트된 위치를 가져옴
        val lastKnownLocation: Location? = locationManager.getLastKnownLocation(locationProvider)
        // 위도 경도 객체로 반환
        if (lastKnownLocation != null) {
            // 위도 경도 객체로 반환
            return LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
        } else {
            // 위치를 구하지 못한경우 기본값 반환
            return CITY_HALL
        }
    }

    //화장실의 위도와 경도 및 이름 들을 저장할 Map 의 List 생성
    var toiletList: MutableList<Map<String, Any>> = mutableListOf<Map<String, Any>>()

    // 화장실 이미지로 사용할 Bitmap
    // Lazy 는 바로 생성하지 않고 처음 사용 될 때 생성하는 문법
    val bitmap by lazy {
        val drawable = resources.getDrawable(R.drawable.restroom_sign, null) as BitmapDrawable
        Bitmap.createScaledBitmap(drawable.bitmap, 64, 64, false)
    }

    // Open API 다운로드 받는 스레드
    //다운로드 받아서 파싱할 스레드
    inner class ToiletThread : Thread() {
        override fun run() {
            val API_KEY =
                "g5i7qJn8Mi5NKv%2FXkSxItQQmoXGzQfgjtj0UdKXYURG4OfE%2BOS%2BxD17cMRFYH22ISNcxiTJw68PboMhNllrnWA%3D%3D"

            //데이터의 시작과 종료 인덱스
            var pageNo = 193
            //과천만
            var numOfRows = 100
            //데이터의 전체 개수를 저장하기 위한 프로퍼티
            //var count = 350

            do {
                //파싱할 URL 생성
                var url =
                    URL(
                        "http://api.data.go.kr/openapi/tn_pubr_public_toilet_api?serviceKey=" + API_KEY + "&pageNo=" + pageNo + "&numOfRows=" + numOfRows + "&type=json"
                    )
                //연결해서 문자열 가져오기
                val connection = url.openConnection()
                val data = connection.getInputStream()
                var isr = InputStreamReader(data)
                // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
                var br = BufferedReader(isr)

                // Json 문서는 일단 문자열로 데이터를 모두 읽어온 후, Json에 관련된 객체를 만들어서 데이터를 가져옴
                var str: String? = null
                var buf = StringBuffer()

                do {
                    str = br.readLine()

                    if (str != null) {
                        buf.append(str)
                    }
                } while (str != null)


                //JSON 파싱
                // 전체가 객체로 묶여있기 때문에 객체형태로 가져옴
                val root = JSONObject(buf.toString())
                val response = root.getJSONObject("response")
                val body = response.getJSONObject("body")
                val items = body.getJSONArray("items") // 객체 안에 있는 item이라는 이름의 리스트를 가져옴


                for (i in 0 until items.length()) {
                    val obj = items.getJSONObject(i)
                    val map = mutableMapOf<String, Any>()
                    map.put("toiletNm", obj.getString("toiletNm"))
                    map.put("lnmadr", obj.getString("lnmadr"))
                    map.put("Lat", obj.optDouble("latitude"))
                    map.put("Lng", obj.optDouble("longitude"))
                    toiletList.add(map)

                }
                //인덱스를 변경해서 데이터 계속 가져오기
                pageNo = pageNo + 1
            } while (pageNo < 194) //일단 과천만 표시하려고
            handler.sendEmptyMessage(0)
        }
    }

    // 스레드가 다운로드 받아서 파싱한 결과를 가지고 맵 뷰에 마커를 출력해달라고 요청
    val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            Log.e("toiletList", toiletList.toString())
            for (map in toiletList) {
                addMarkers(map as MutableMap<String, Any>)
            }
        }
    }

    // 마커를 추가하는 함수
    @SuppressLint("PotentialBehaviorOverride")
    fun addMarkers(toilet: MutableMap<String, Any>) {
        // 맵이 직접 마커를 생성 - 작은 지역에 마커가 많으면 보기가 안좋습니다.
        // 마커 누르면 하단시트


        googleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(toilet.get("Lat") as Double, toilet.get("Lng") as Double))
                .title(toilet.get("toiletNm") as String)
                .snippet(toilet.get("lnmadr") as String)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        )

        googleMap?.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener { marker -> // TODO Auto-generated method stub
            if (true) {
                val bottomSheet = BottomSheet()
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                googleMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(marker.position, DEFAULT_ZOOM_LEVEL))
                return@OnMarkerClickListener true
            }
            false
        })
    }

    var toiletThread: ToiletThread? = null

    // 앱이 활성화될때 서울시 데이터를 읽어옴
    override fun onStart() {
        super.onStart()
        if (toiletThread == null) {
            toiletThread = ToiletThread()
            toiletThread!!.start()
        }
    }

    // 앱이 비활성화 될때 백그라운드 작업 취소
    override fun onStop() {
        super.onStop()
        toiletThread!!.isInterrupted
        toiletThread = null
    }
}