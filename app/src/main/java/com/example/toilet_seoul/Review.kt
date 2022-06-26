package com.example.toilet_seoul

import android.annotation.SuppressLint
import android.media.Rating
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Review : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet)

        val database: DatabaseReference = Firebase.database.reference
        val currentUser = Firebase.auth.currentUser
        val reviewRef = database.child("toilet").child(R.id.toiletNum.toString()).child("review")

        var contentText = findViewById<EditText>(R.id.contentText).text
        var rate = findViewById<RatingBar>(R.id.ratingBar).rating
        var curUID = currentUser?.uid.toString()

        //리뷰 쓰기
        findViewById<Button>(R.id.confirmButton).setOnClickListener {
            if (contentText.toString() != "" || rate > 0){
                //toiletNum-review에 리뷰 저장
                reviewRef.child(curUID).child("rate").setValue(rate.toString())
                reviewRef.child(curUID).child("content").setValue(contentText.toString())

                Toast.makeText(this.applicationContext, "리뷰가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                rate = 0.0F
                contentText.clear()

                //리뷰 바텀시트 하단에 불러오기(아래 Unresolved Ref들은 레이아웃에 아이디 추가해야 함)
                reviewRef.addListenerForSingleValueEvent(object :ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //userNm: 사용자 닉네임
                        findViewById<TextView>(R.id.userNm).text = snapshot.child(curUID).key
                        //rate: 별점
                        findViewById<RatingBar>(R.id.rate).rating = snapshot.child(curUID).child("rate").value as Float
                        //contentR: 리뷰내용
                        findViewById<TextView>(R.id.contentR).text = snapshot.child(curUID).child("content").value.toString()

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            } else {
                Toast.makeText(this.applicationContext, "별점이나 리뷰를 작성해주세요.", Toast.LENGTH_SHORT).show()
            }
        }


    }
}