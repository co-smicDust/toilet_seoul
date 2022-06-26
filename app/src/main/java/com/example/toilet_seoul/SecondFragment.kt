package com.example.toilet_seoul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SecondFragment : Fragment(), MainActivity.onBackPressedListener{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.contact_setting, container, false)
    }
    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        //requireActivity().supportFragmentManager.popBackStack()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database: DatabaseReference = Firebase.database.reference
        val currentUser = Firebase.auth.currentUser
        val userRef = database.child("User").child(currentUser?.uid.toString()).child("emgContact")

        view.findViewById<Button>(R.id.add_button).setOnClickListener {
            val inputNm = view.findViewById<EditText>(R.id.add_edittext_name).text
            val inputNum = view.findViewById<EditText>(R.id.add_edittext_number).text

            userRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var dupVal: Boolean = false     //중복확인변수

                    //이름과 연락처를 다 적으면
                    if (inputNm.toString() != "" && inputNum.toString() != "") {
                        //중복 검사를 한다
                        children@ for(i in dataSnapshot.children.iterator()){
                            if(inputNm.toString() == i.key || inputNum.toString() == i.value){
                                Toast.makeText(context, "중복된 연락처입니다.", Toast.LENGTH_SHORT).show()
                                dupVal = true
                                break@children
                            }
                        }
                        //중복이 아니면 데이터 저장하고 화면에 추가
                        if(!dupVal)
                            userRef.child(inputNm.toString()).setValue(inputNum.toString()).addOnSuccessListener {
                                Toast.makeText(context, "비상연락처가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                inputNm.clear()
                                inputNum.clear()

                            }
                    } else {    //이름이나 연락처를 적지 않으면
                        Toast.makeText(context, "비상연락처의 이름과 전화번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        }

    }
}