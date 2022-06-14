package com.example.toilet_seoul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import kotlin.concurrent.thread

data class Toilet(val toiletNm: String? = null, val rdnmadr: String? = null, val lnmadr: String? = null, val unisexToiletYn: String? = null,
                  val menToiletBowlNumber: Int? = null, val menUrineNumber: Int? = null, val menHandicapToiletBowlNumber: Int? = null,
                  val menHandicapUrinalNumber: Int? = null, val menChildrenToiletBowlNumber: Int? = null, val menChildrenUrinalNumber: Int? = null,
                  val ladiesToiletBowlNumber: Int? = null, val ladiesHandicapToiletBowlNumber: Int? = null, val ladiesChildrenToiletBowlNumber: Int? = null,
                  val phoneNumber: String? = null, val openTime: String? = null, val latitude: Double? = null, val longitude: Double? = null,
                  val emgBellYn: String? = null, val enterentCctvYn: String? = null, val dipersExchgPosi: String? = null) {

}

class RealtimeDB : AppCompatActivity() {

    val database: DatabaseReference = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realtime_db)

        thread {
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
                var str: String ?= null
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

                    val toilet = Toilet(obj.getString("toiletNm"),
                        obj.getString("rdnmadr"),
                        obj.getString("lnmadr"),
                        obj.getString("unisexToiletYn"),
                        obj.getInt("menToiletBowlNumber"),
                        obj.getInt("menUrineNumber"),
                        obj.getInt("menHandicapToiletBowlNumber"),
                        obj.getInt("menHandicapUrinalNumber"),
                        obj.getInt("menChildrenToiletBowlNumber"),
                        obj.getInt("menChildrenUrinalNumber"),
                        obj.getInt("ladiesToiletBowlNumber"),
                        obj.getInt("ladiesHandicapToiletBowlNumber"),
                        obj.getInt("ladiesChildrenToiletBowlNumber"),
                        obj.getString("phoneNumber"),
                        obj.getString("openTime"),
                        obj.getString("latitude").toDoubleOrNull(),
                        obj.getString("longitude").toDoubleOrNull(),
                        obj.getString("emgBellYn"),
                        obj.getString("enterentCctvYn"),
                        obj.getString("dipersExchgPosi"))

                    database.child("toilet").child(i.toString()).setValue(toilet)

                }
                //인덱스를 변경해서 데이터 계속 가져오기
                pageNo = pageNo + 1
            } while (pageNo < 194) //일단 과천만 표시하려고


            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}