package com.example.toilet_seoul

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class DangerCall : AppCompatActivity() {
    val mContext = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.danger_call)

        findViewById<Button>(R.id.dialBtn).setOnClickListener {

            val input = findViewById<EditText>(R.id.phoneNumEdt).text.toString()
            val myUri = Uri.parse("tel:${input}")
            val myIntent = Intent(Intent.ACTION_DIAL, myUri)
            startActivity(myIntent)
        }

        //앱에서 바로 전화걸기 - 권한 문제로 수정중
        /* callBtn.setOnClickListener {
            // 어디에 전화를 걸건지 text 정보 받기
            val input = phoneNumEdt.text.toString()
            val permissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    val myUri = Uri.parse("tel:${input}")
                    val myIntent = Intent(Intent.ACTION_CALL, myUri)
                    startActivity(myIntent)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(mContext, "전화 연결 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }

            }

            TedPermission.with(mContext)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("[설정] 에서 권한을 열어줘야 전화 연결이 가능합니다.")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check()
        }*/


        findViewById<Button>(R.id.smsBtn).setOnClickListener {
            val inputPhoneNum = findViewById<EditText>(R.id.phoneNumEdt).text.toString()
            val myUri = Uri.parse("smsto:${inputPhoneNum}")
            val myIntent = Intent(Intent.ACTION_SENDTO, myUri)
            // 문자 전송 화면 이동 시 미리 문구를 적어서 보내기
            // myIntent를 가지고 갈 때 -> putExtra로 데이터를 담아서 보내자
            myIntent.putExtra("sms_body", "위급 상황입니다.")
            startActivity(myIntent)
        }

    }
}